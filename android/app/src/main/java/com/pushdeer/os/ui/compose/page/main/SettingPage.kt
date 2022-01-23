package com.pushdeer.os.ui.compose.page.main

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pushdeer.os.R
import com.pushdeer.os.holder.RequestHolder
import com.pushdeer.os.ui.compose.componment.SettingItem
import com.pushdeer.os.ui.navigation.Page

@ExperimentalMaterialApi
@Composable
fun SettingPage(requestHolder: RequestHolder) {
    MainPageFrame(
        titleStringId = Page.Settings.labelStringId,
        showSideIcon = false
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
//                var newName by remember {
//                    mutableStateOf(requestHolder.pushDeerViewModel.userInfo.name)
//                }
                SettingItem(
                    text = "${stringResource(id = R.string.main_setting_user_hi)} ${requestHolder.pushDeerViewModel.userInfo.name} !",
                    buttonString = stringResource(id = R.string.main_setting_user_logout),
                    onItemClick = {

//                        requestHolder.alert.alert(
//                            title = "修改用户名",
//                            content = {
//                                TextField(
//                                    value = newName,
//                                    onValueChange = { newName = it },
//                                    colors = TextFieldDefaults.textFieldColors(
//                                        focusedIndicatorColor = Color.Transparent,
//                                        unfocusedIndicatorColor = Color.Transparent,
//                                        disabledIndicatorColor = Color.Transparent,
//                                        errorIndicatorColor = Color.Transparent,
//                                    )
//                                )
//                            },
//                            onOk = {
//
//                            }
//                        )
                    }
                ) {
                    requestHolder.pushDeerViewModel.deviceList.filter { it.device_id == requestHolder.settingStore.thisDeviceId }
                        .forEach {
                            requestHolder.device.deviceRemove(it)
                        }
                    requestHolder.settingStore.userToken = ""
                    requestHolder.globalNavController.navigate("login") {
                        requestHolder.globalNavController.popBackStack()
                    }
                    requestHolder.alert.alert(
                        R.string.global_alert_title_alert,
                        R.string.main_setting_alert_logout,
                        {}
                    )
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
                    val uri = Uri.parse("market://details?id=" + "com.pushdeer.os")
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
        }
    }
}

//@Composable
//fun TogglePreferenceItem(label: String) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Text(text = label)
//    }
//}