package com.pushdeer.os.ui.compose.page.main

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pushdeer.os.R
import com.pushdeer.os.MainActivity
import com.pushdeer.os.data.api.data.response.UserInfo
import com.pushdeer.os.holder.RequestHolder
import com.pushdeer.os.ui.compose.componment.SettingItem
import com.pushdeer.os.ui.navigation.Page
import com.pushdeer.os.ui.theme.MBlue
import com.pushdeer.os.ui.theme.MainBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun SettingPage(requestHolder: RequestHolder) {
    MainPageFrame(
        titleStringId = Page.Settings.labelStringId,
        showSideIcon = false
    ) {
        var showLoginMethod by remember {
            mutableStateOf(false)
        }

        // 两个分组的展开状态提到最外层：列表滚动时 item 会被回收，放在 item 里会莫名收起
        var keepAliveExpanded by remember { mutableStateOf(false) }
        var otherExpanded by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            requestHolder.coroutineScope.launch {
                delay(300)
                showLoginMethod = true
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Card(
                    elevation = 5.dp,
                    modifier = Modifier.padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, MainBlue)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        SettingItem(
                            text = "${stringResource(id = R.string.main_setting_user_hi)} ${requestHolder.pushDeerViewModel.userInfo.name} !",
                            buttonString = stringResource(id = R.string.main_setting_user_logout),
                            paddingValues = PaddingValues(bottom = 0.dp),
                            onItemClick = {}
                        ) {
                            requestHolder.pushDeerViewModel.deviceList
                                .filter { it.device_id == requestHolder.settingStore.thisDeviceId }
                                .forEach { requestHolder.device.deviceRemove(it) }
                            requestHolder.settingStore.userToken = ""
                            requestHolder.pushDeerViewModel.token = ""  // 同时清空内存登录态，否则重新登录会被旧token短路
                            requestHolder.globalNavController.navigate("login") {
                                requestHolder.globalNavController.popBackStack()
                                requestHolder.pushDeerViewModel.userInfo = UserInfo()
                            }
                            requestHolder.alert.alert(
                                title = R.string.global_alert_title_alert,
                                content = R.string.main_setting_alert_logout,
                                onOk = {}
                            )
                        }
                        AnimatedVisibility(
                            visible = showLoginMethod,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {

                            // LoginMethod Row
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 6.dp, start = 6.dp, end = 6.dp, top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                // wx 登陆按钮
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .weight(0.5F)
                                        .clickable {
                                            if (requestHolder.pushDeerViewModel.userInfo.isWeChatLogin) {
                                                requestHolder.alert.alert(
                                                    title = "Hey",
                                                    content = "你已经通过 微信账号 成功登陆咯！",
                                                    onOk = {})
                                            } else {
                                                requestHolder.alert.alert(
                                                    title = "绑定或迁移由 微信账号 创建的账号",
                                                    content = "请注意，如果你将要登陆的 微信账号 已经在此登陆过，其设备列表将会与当前账号合并， PushKey 将会在合并时丢失。",
                                                    onOk = requestHolder.weChatLogin.login
                                                )
                                            }
                                        }
                                ) {
                                    LoginMethod(
                                        isLogin = requestHolder.pushDeerViewModel.userInfo.isWeChatLogin,
                                        id = R.drawable.ic_wechat_colored
                                    )

                                }

                                // apple 登陆按钮
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .weight(0.5F)
                                        .clickable {
                                            if (requestHolder.pushDeerViewModel.userInfo.isAppleLogin) {
                                                requestHolder.alert.alert(
                                                    title = "Hey",
                                                    content = "你已经通过 Apple Id 成功登陆咯！",
                                                    onOk = {})
                                            } else {
                                                requestHolder.alert.alert(
                                                    title = "绑定或迁移由 Apple Id 创建的账号",
                                                    content = "请注意，如果你将要用来登陆 PushDeer 的 Apple Id 已经在此登陆过，其设备列表将会与当前账号合并， PushKey 将会在合并时丢失。",
                                                    onOk = requestHolder.appleLogin.login
                                                )
                                            }
                                        }
                                ) {
                                    LoginMethod(
                                        isLogin = requestHolder.pushDeerViewModel.userInfo.isAppleLogin,
                                        id = R.drawable.ic_apple_colored
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 原版自带的 Use Inner WebView 开关，保持原位置（重度用户偶尔要用）
            item {
                var useInnerWebView by remember {
                    mutableStateOf(requestHolder.settingStore.useInnerWebView)
                }

                val bgc by animateColorAsState(
                    targetValue = if (useInnerWebView) MaterialTheme.colors.MBlue else Color.Transparent
                )
                val bdc by animateColorAsState(targetValue = if (!useInnerWebView) MaterialTheme.colors.MBlue else Color.Transparent)
                val fgc by animateColorAsState(targetValue = if (useInnerWebView) Color.White else MaterialTheme.colors.MBlue)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .border(1.dp, color = bdc, shape = RoundedCornerShape(10.dp))
                        .clickable {
                            useInnerWebView = !useInnerWebView
                            requestHolder.settingStore.useInnerWebView = useInnerWebView
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(0.7F)
                            .height(60.dp)
                            .background(color = bgc, shape = RoundedCornerShape(10.dp)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Use Inner WebView", color = fgc, fontSize = 18.sp)
                        AnimatedVisibility(visible = useInnerWebView) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "",
                                tint = fgc
                            )
                        }
                    }
                    AnimatedVisibility(visible = !useInnerWebView) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "",
                            tint = fgc,
                            modifier = Modifier.padding(horizontal = 22.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ===== 分组一：保活增强（内含「开机自启/网络自愈」与「隐藏最近任务」） =====
            item {
                Column {
                    SettingGroupHeader(
                        title = "保活增强",
                        expanded = keepAliveExpanded,
                        onToggle = { keepAliveExpanded = !keepAliveExpanded }
                    )
                    AnimatedVisibility(
                        visible = keepAliveExpanded,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(8.dp))

                            var keepAliveEnabled by remember {
                                mutableStateOf(requestHolder.settingStore.keepAliveEnabled)
                            }
                            SettingToggleRow(
                                label = "开机自启 / 网络自愈",
                                checked = keepAliveEnabled,
                                onChange = { enabled ->
                                    keepAliveEnabled = enabled
                                    requestHolder.settingStore.keepAliveEnabled = enabled
                                }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            var hideFromRecents by remember {
                                mutableStateOf(requestHolder.settingStore.hideFromRecents)
                            }
                            SettingToggleRow(
                                label = "隐藏最近任务",
                                checked = hideFromRecents,
                                onChange = { enabled ->
                                    hideFromRecents = enabled
                                    requestHolder.settingStore.hideFromRecents = enabled
                                    // 即时应用最近任务隐藏（参考 gkd 方案）
                                    (requestHolder.myActivity as? MainActivity)
                                        ?.applyExcludeFromRecents(enabled)
                                }
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ===== 分组二：其他设置（内含「轮询间隔」与「失败提醒」） =====
            item {
                Column {
                    SettingGroupHeader(
                        title = "其他设置",
                        expanded = otherExpanded,
                        onToggle = { otherExpanded = !otherExpanded }
                    )
                    AnimatedVisibility(
                        visible = otherExpanded,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(8.dp))

                            // 轮询间隔
                            var intervalSec by remember {
                                mutableStateOf(requestHolder.settingStore.pollIntervalSeconds)
                            }
                            var showIntervalDialog by remember { mutableStateOf(false) }
                            val intervalOptions = listOf(
                                30 to "30秒", 60 to "1分钟", 180 to "3分钟", 300 to "5分钟",
                                600 to "10分钟", 1800 to "30分钟", 3600 to "1小时",
                                10800 to "3小时", 21600 to "6小时"
                            )
                            val currentLabel = intervalOptions
                                .firstOrNull { it.first == intervalSec }?.second ?: "${intervalSec}秒"
                            SettingChoiceRow(text = "轮询间隔：$currentLabel") {
                                showIntervalDialog = true
                            }
                            if (showIntervalDialog) {
                                AlertDialog(
                                    onDismissRequest = { showIntervalDialog = false },
                                    title = { Text("轮询间隔") },
                                    text = {
                                        Column {
                                            intervalOptions.forEach { (sec, label) ->
                                                Text(
                                                    text = if (sec == intervalSec) "$label ✓" else label,
                                                    fontSize = 18.sp,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable {
                                                            intervalSec = sec
                                                            requestHolder.settingStore.pollIntervalSeconds = sec
                                                            showIntervalDialog = false
                                                        }
                                                        .padding(vertical = 12.dp)
                                                )
                                            }
                                        }
                                    },
                                    confirmButton = {},
                                    dismissButton = {
                                        TextButton(onClick = { showIntervalDialog = false }) {
                                            Text("取消", color = Color(0xFF3B4789))
                                        }
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // 失败提醒
                            var remindHours by remember {
                                mutableStateOf(requestHolder.settingStore.failureRemindHours)
                            }
                            var showRemindDialog by remember { mutableStateOf(false) }
                            val remindOptions = listOf(
                                0 to "关闭", 1 to "1小时", 3 to "3小时", 6 to "6小时"
                            )
                            val remindLabel = remindOptions
                                .firstOrNull { it.first == remindHours }?.second ?: "${remindHours}小时"
                            SettingChoiceRow(text = "失败提醒：$remindLabel") {
                                showRemindDialog = true
                            }
                            if (showRemindDialog) {
                                AlertDialog(
                                    onDismissRequest = { showRemindDialog = false },
                                    title = { Text("失败提醒") },
                                    text = {
                                        Column {
                                            remindOptions.forEach { (hours, label) ->
                                                Text(
                                                    text = if (hours == remindHours) "$label ✓" else label,
                                                    fontSize = 18.sp,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable {
                                                            remindHours = hours
                                                            requestHolder.settingStore.failureRemindHours = hours
                                                            showRemindDialog = false
                                                        }
                                                        .padding(vertical = 12.dp)
                                                )
                                            }
                                        }
                                    },
                                    confirmButton = {},
                                    dismissButton = {
                                        TextButton(onClick = { showRemindDialog = false }) {
                                            Text("取消", color = Color(0xFF3B4789))
                                        }
                                    }
                                )
                            }

                            Text(
                                text = "收到含「失败」「失效」的推送后，按上面的间隔重复提醒；收到含「成功」的推送自动停止。",
                                fontSize = 14.sp,
                                color = Color(0xFF888888),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ===== 原版条目：喜欢 PushDeer 吗 / LogDog 移到底部 =====
            item {
                SettingItem(
                    text = stringResource(id = R.string.main_setting_douyoulike),
                    buttonString = stringResource(id = R.string.main_setting_btn_like)
                ) {
                    val uri = Uri.parse("market://details?id=com.pushdeer.os")
                    Intent(Intent.ACTION_VIEW, uri).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }.let {
                        try {
                            requestHolder.myActivity.startActivity(it)
                        } catch (e: Exception) {
                            Log.d("WH_", "SettingPage: ${e.localizedMessage}")
                        }
                    }
                }
            }

            item {
                SettingItem(
                    text = stringResource(id = R.string.main_setting_logdog),
                    buttonString = stringResource(id = R.string.main_setting_logdog_open)
                ) {
                    requestHolder.globalNavController.navigate("logdog")
                }
            }
        }
    }
}

/**
 * 分组标题行：点一下展开 / 收起下面的内容。
 */
@Composable
private fun SettingGroupHeader(title: String, expanded: Boolean, onToggle: () -> Unit) {
    // 展开时整行变成"开关打开"的深色样式（蓝底白字），收起时是蓝框空心
    val bg by animateColorAsState(
        targetValue = if (expanded) MaterialTheme.colors.MBlue else Color.Transparent
    )
    val bd by animateColorAsState(
        targetValue = if (!expanded) MaterialTheme.colors.MBlue else Color.Transparent
    )
    val fg by animateColorAsState(
        targetValue = if (expanded) Color.White else MaterialTheme.colors.MBlue
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = bg, shape = RoundedCornerShape(10.dp))
            .border(1.dp, color = bd, shape = RoundedCornerShape(10.dp))
            .clickable { onToggle() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = fg,
            fontSize = 18.sp,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        )
        Text(
            text = if (expanded) "▲" else "▼",
            color = fg,
            fontSize = 14.sp,
            modifier = Modifier.padding(end = 16.dp)
        )
    }
}

/**
 * 开关行：沿用 App 原有的样式（蓝底 + 对勾 / 空框 + 叉号）。
 */
@Composable
private fun SettingToggleRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    val bg by animateColorAsState(
        targetValue = if (checked) MaterialTheme.colors.MBlue else Color.Transparent
    )
    val bd by animateColorAsState(targetValue = if (!checked) MaterialTheme.colors.MBlue else Color.Transparent)
    val fg by animateColorAsState(targetValue = if (checked) Color.White else MaterialTheme.colors.MBlue)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .border(1.dp, color = bd, shape = RoundedCornerShape(10.dp))
            .clickable { onChange(!checked) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(0.7F)
                .height(60.dp)
                .background(color = bg, shape = RoundedCornerShape(10.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = label, color = fg, fontSize = 18.sp)
            AnimatedVisibility(visible = checked) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "",
                    tint = fg
                )
            }
        }
        AnimatedVisibility(visible = !checked) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "",
                tint = fg,
                modifier = Modifier.padding(horizontal = 22.dp)
            )
        }
    }
}

/**
 * 选择行：右侧值由调用方拼进 label，点一下弹选择框。
 */
@Composable
private fun SettingChoiceRow(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .border(1.dp, color = MaterialTheme.colors.MBlue, shape = RoundedCornerShape(10.dp))
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = MaterialTheme.colors.MBlue,
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun IconYes() {
    Icon(
        painter = painterResource(id = R.drawable.ic_okok2),
        contentDescription = "",
        tint = MainBlue,
        modifier = Modifier.size(20.dp)
    )
}

@Composable
fun IconNo() {
    Icon(
        painter = painterResource(id = R.drawable.ic_okok2),
        contentDescription = "",
        tint = Color.Gray,
        modifier = Modifier.size(20.dp)
    )
}

@Composable
fun LoginMethod(isLogin: Boolean, @DrawableRes id: Int) {
    Row(
        modifier = Modifier.width(70.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isLogin)
            IconYes()
        else
            IconNo()
        Image(
            painter = painterResource(id = id),
            contentDescription = "",
            modifier = Modifier
                .padding(vertical = 12.dp)
                .size(35.dp),
        )
    }
}
