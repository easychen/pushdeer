package com.pushdeer.os.ui.compose.page.main

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                SettingItem(
                    text = "Hi ${requestHolder.pushDeerViewModel.userInfo.name} !",
                    buttonString = "Logout"
                ) {
                    requestHolder.settingStore.userToken = ""
                    // logout 操作：
                    // 从服务器删除本设备
                    // 删除保存的 token
                }
            }
            item {
                SettingItem(
                    text = "Customize Server",
                    buttonString = "Scan QR"
                ) {
                    requestHolder.startQrScanActivity()
                }
            }
            item {
                SettingItem(
                    text = "Do you like PushDeer ?",
                    buttonString = "Like"
                ) {
                }
            }

            item {
                SettingItem(
                    text = "LogDog",
                    buttonString = "Open"
                ) {
                    requestHolder.globalNavController.navigate("logdog")
                }
            }
        }
    }
}

@Composable
fun TogglePreferenceItem(label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = label)
    }
}