package com.pushdeer.os.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * 开机自启 + 网络变化时重启轮询服务，保证推送服务持续在线。
 */
class PollingServiceStarter : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action ?: return
        val app = context.applicationContext
        val setting = (app as com.pushdeer.os.App).storeKeeper.settingStore
        if (setting.userToken.isEmpty()) return
        // 保活增强开关：关闭时不执行开机自启/网络自愈
        if (!setting.keepAliveEnabled) return
        when (action) {
            Intent.ACTION_BOOT_COMPLETED,
            android.net.ConnectivityManager.CONNECTIVITY_ACTION -> {
                PollingService.start(app)
            }
        }
    }
}
