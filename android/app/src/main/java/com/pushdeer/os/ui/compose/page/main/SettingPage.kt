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
//            item {
//                SettingItem(
//                    text = "Customize Server",
//                    buttonString = "Scan QR"
//                ) {
//                    requestHolder.startQrScanActivity()
//                }
//            }
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
//                            requestHolder.logDogViewModel
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
                        Text(text = "Use Inner WebView", color = fgc,fontSize = 18.sp)
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

            item {
                var keepAliveEnabled by remember {
                    mutableStateOf(requestHolder.settingStore.keepAliveEnabled)
                }

                val kBg by animateColorAsState(
                    targetValue = if (keepAliveEnabled) MaterialTheme.colors.MBlue else Color.Transparent
                )
                val kBd by animateColorAsState(targetValue = if (!keepAliveEnabled) MaterialTheme.colors.MBlue else Color.Transparent)
                val kFg by animateColorAsState(targetValue = if (keepAliveEnabled) Color.White else MaterialTheme.colors.MBlue)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .border(1.dp, color = kBd, shape = RoundedCornerShape(10.dp))
                        .clickable {
                            keepAliveEnabled = !keepAliveEnabled
                            requestHolder.settingStore.keepAliveEnabled = keepAliveEnabled
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(0.7F)
                            .height(60.dp)
                            .background(color = kBg, shape = RoundedCornerShape(10.dp)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "保活增强", color = kFg, fontSize = 18.sp)
                        AnimatedVisibility(visible = keepAliveEnabled) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "",
                                tint = kFg
                            )
                        }
                    }
                    AnimatedVisibility(visible = !keepAliveEnabled) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "",
                            tint = kFg,
                            modifier = Modifier.padding(horizontal = 22.dp)
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                var hideFromRecents by remember {
                    mutableStateOf(requestHolder.settingStore.hideFromRecents)
                }

                val hBg by animateColorAsState(
                    targetValue = if (hideFromRecents) MaterialTheme.colors.MBlue else Color.Transparent
                )
                val hBd by animateColorAsState(targetValue = if (!hideFromRecents) MaterialTheme.colors.MBlue else Color.Transparent)
                val hFg by animateColorAsState(targetValue = if (hideFromRecents) Color.White else MaterialTheme.colors.MBlue)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .border(1.dp, color = hBd, shape = RoundedCornerShape(10.dp))
                        .clickable {
                            hideFromRecents = !hideFromRecents
                            requestHolder.settingStore.hideFromRecents = hideFromRecents
                            // 即时应用最近任务隐藏（参考 gkd 方案）
                            (requestHolder.myActivity as? MainActivity)
                                ?.applyExcludeFromRecents(hideFromRecents)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(0.7F)
                            .height(60.dp)
                            .background(color = hBg, shape = RoundedCornerShape(10.dp)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "隐藏最近任务", color = hFg, fontSize = 18.sp)
                        AnimatedVisibility(visible = hideFromRecents) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "",
                                tint = hFg
                            )
                        }
                    }
                    AnimatedVisibility(visible = !hideFromRecents) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "",
                            tint = hFg,
                            modifier = Modifier.padding(horizontal = 22.dp)
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .border(1.dp, color = MaterialTheme.colors.MBlue, shape = RoundedCornerShape(10.dp))
                        .clickable { showIntervalDialog = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "轮询间隔：$currentLabel",
                        color = MaterialTheme.colors.MBlue,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
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
            }        }
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