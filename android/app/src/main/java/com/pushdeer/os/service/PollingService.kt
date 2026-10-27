package com.pushdeer.os.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.pushdeer.os.App
import com.pushdeer.os.MainActivity
import com.pushdeer.os.data.api.PushDeerApi
import com.pushdeer.os.data.api.data.response.Message
import com.pushdeer.os.data.repository.MessageRepository
import com.pushdeer.os.store.SettingStore
import com.pushdeer.os.util.NotificationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * 后台轮询服务：定时拉取 PushDeer 消息列表，
 * 发现新消息后用安卓系统通知弹出。
 * 替代已废弃的小米推送通道。
 */
class PollingService : Service() {

    companion object {
        private const val TAG = "PollingService"
        private const val SERVICE_NOTIFICATION_ID = 1001
        private const val MESSAGE_NOTIFICATION_ID = 1002
        private const val FAILURE_REMIND_NOTIFICATION_ID = 1003
        private const val SERVICE_CHANNEL_ID = "polling_service"

        // 失败提醒的检查节拍（60 秒一次，与轮询间隔无关，保证提醒按小时准点）
        private const val FAILURE_CHECK_INTERVAL_MS = 60_000L

        // 关键字：命中失败词进入失败状态；命中成功词退出
        private val FAILURE_KEYWORDS = listOf("失败", "失效")
        private const val SUCCESS_KEYWORD = "成功"

        fun start(context: Context) {
            val intent = Intent(context, PollingService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var api: PushDeerApi
    private lateinit var settingStore: SettingStore
    private lateinit var messageRepository: MessageRepository
    private var lastMaxId: Int = -1
    private var consecutiveFailures = 0
    private var lastInvalidNotifiedToken = ""

    override fun onCreate() {
        super.onCreate()
        val app = application as App
        api = app.apiService
        settingStore = app.storeKeeper.settingStore
        messageRepository = app.repositoryKeeper.messageRepository

        NotificationUtil.setupChannel(this)
        createServiceChannel()
        startForeground(SERVICE_NOTIFICATION_ID, buildServiceNotification())

        scope.launch {
            // 首次拉取：记录当前最大消息ID，不通知历史消息
            try {
                val first = api.messageList(settingStore.userToken)
                first.content?.messages?.forEach { messageRepository.insert(it) }
                lastMaxId = first.content?.messages?.maxOfOrNull { it.id } ?: -1
            } catch (e: Exception) {
                // 网络异常忽略，下轮重试
            }
            while (isActive) {
                // 轮询间隔可在设置页选择（30秒~6小时），每次循环读取最新值
                delay(settingStore.pollIntervalSeconds * 1000L)
                try {
                    poll()
                } catch (e: Exception) {
                    // 网络异常静默，下一轮再试
                }
            }
        }

        // 失败提醒：独立节拍检查，不受轮询间隔影响（轮询可能设成 6 小时）
        scope.launch {
            while (isActive) {
                delay(FAILURE_CHECK_INTERVAL_MS)
                try {
                    checkFailureReminder()
                } catch (e: Exception) {
                    // 忽略，下一拍再试
                }
            }
        }
    }

    private suspend fun poll() {
        val token = settingStore.userToken
        if (token.isEmpty()) {
            stopSelf()
            return
        }
        val resp = api.messageList(token)
        if (resp.code != 0) {
            // 业务错误（如 token 失效）：连续 3 次判定登录失效
            consecutiveFailures++
            if (consecutiveFailures >= 3) {
                handleInvalidToken(token)
            }
            return
        }
        consecutiveFailures = 0
        val messages = resp.content?.messages ?: return
        if (messages.isEmpty()) return

        val newMax = messages.maxOfOrNull { it.id } ?: return
        if (newMax <= lastMaxId) return // 无新消息

        val news = messages.filter { it.id > lastMaxId }.sortedBy { it.id }
        for (m in news) {
            showNotification(m)
            updateFailureState(m)
            messageRepository.insert(m)
        }
        lastMaxId = newMax
    }

    /**
     * 关键字驱动的失败提醒状态机：
     * - 命中「失败」「失效」→ 进入（或刷新）失败状态，从此刻开始计时
     * - 命中「成功」→ 退出失败状态，停止后续提醒
     * 状态存进 SettingStore，进程被杀重启后仍然有效。
     */
    private fun updateFailureState(message: Message) {
        val raw = listOfNotNull(message.text, message.desp)
            .joinToString(" ")
            .trim()
        if (raw.isEmpty()) return

        if (raw.contains(SUCCESS_KEYWORD)) {
            clearFailureState()
            return
        }
        if (FAILURE_KEYWORDS.none { raw.contains(it) }) return

        val now = System.currentTimeMillis()
        settingStore.failureSince = now
        // 触发的那条推送本身已经弹过通知，所以提醒计时从此刻开始
        settingStore.failureLastNotifyAt = now
        settingStore.failureMessage = raw.take(80)
    }

    private fun clearFailureState() {
        settingStore.failureSince = 0L
        settingStore.failureLastNotifyAt = 0L
        settingStore.failureMessage = ""
    }

    /**
     * 如果处于失败状态且距上次提醒已超过设定间隔，就再提醒一次。
     * 间隔在设置页可选 1/3/6 小时，0 表示关闭。
     */
    private fun checkFailureReminder() {
        val hours = settingStore.failureRemindHours
        if (hours <= 0) return
        val since = settingStore.failureSince
        if (since <= 0L) return

        val now = System.currentTimeMillis()
        val intervalMs = hours * 60L * 60L * 1000L
        if (now - settingStore.failureLastNotifyAt < intervalMs) return

        settingStore.failureLastNotifyAt = now
        showFailureReminderNotification(hours, since, now)
    }

    private fun showFailureReminderNotification(hours: Int, since: Long, now: Long) {
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val elapsedMin = (now - since) / 60_000L
        val elapsed = when {
            elapsedMin < 60 -> "$elapsedMin 分钟"
            elapsedMin < 24 * 60 -> "${elapsedMin / 60} 小时"
            else -> "${elapsedMin / (24 * 60)} 天"
        }
        val reason = settingStore.failureMessage
        val text = if (reason.isEmpty()) {
            "已持续 $elapsed，每 $hours 小时提醒一次；收到含「成功」的推送后停止"
        } else {
            "已持续 $elapsed · 触发消息：$reason"
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pi = PendingIntent.getActivity(
            this,
            9002,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = Notification.Builder(this, NotificationUtil.HIGH_CHANNEL_ID)
            .setContentTitle("推送提示：仍处于失败状态")
            .setContentText(text)
            .setStyle(Notification.BigTextStyle().bigText(text))
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .setAutoCancel(true)
            .setContentIntent(pi)
            .build()
        nm.notify(FAILURE_REMIND_NOTIFICATION_ID, notification)
    }

    /**
     * 检测到登录失效：清登录态并提醒用户重新登录（同一失效 token 只提醒一次）。
     */
    private fun handleInvalidToken(token: String) {
        if (lastInvalidNotifiedToken == token) return
        lastInvalidNotifiedToken = token
        // 清登录态：App 下次打开会直接进入登录页
        settingStore.userToken = ""
        // 弹提醒通知
        showInvalidTokenNotification()
        // 停止轮询（重新登录成功后会自动重启）
        stopSelf()
    }

    private fun showInvalidTokenNotification() {
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pi = PendingIntent.getActivity(
            this,
            9001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = Notification.Builder(this, NotificationUtil.HIGH_CHANNEL_ID)
            .setContentTitle("PushDeer 登录已失效")
            .setContentText("点击重新登录，恢复消息通知")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setAutoCancel(true)
            .setContentIntent(pi)
            .build()
        nm.notify(9001, notification)
    }

    private fun showNotification(message: Message) {
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val title = message.text ?: "PushDeer 消息"
        val content = if (!message.desp.isNullOrEmpty()) message.desp else message.text
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pi = PendingIntent.getActivity(
            this,
            message.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = Notification.Builder(this, NotificationUtil.HIGH_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .setContentIntent(pi)
            .build()
        nm.notify(MESSAGE_NOTIFICATION_ID, notification)
    }

    private fun createServiceChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                SERVICE_CHANNEL_ID,
                "推送服务",
                NotificationManager.IMPORTANCE_MIN
            )
            nm.createNotificationChannel(channel)
        }
    }

    private fun buildServiceNotification(): Notification {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, SERVICE_CHANNEL_ID)
        } else {
            Notification.Builder(this)
        }
        return builder
            .setContentTitle("PushDeer 推送服务运行中")
            .setContentText("正在接收消息通知")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 保持服务，被系统回收后尝试重建
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
