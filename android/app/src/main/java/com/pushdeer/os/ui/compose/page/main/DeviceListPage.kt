package com.pushdeer.os.ui.compose.page.main

import android.os.Build
import android.text.TextUtils
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pushdeer.os.R
import com.pushdeer.os.data.api.data.request.DeviceInfo
import com.pushdeer.os.data.api.data.response.UserInfo
import com.pushdeer.os.holder.RequestHolder
import com.pushdeer.os.ui.compose.componment.CardItemSingleLineWithIcon
import com.pushdeer.os.ui.compose.componment.ListBottomBlankItem
import com.pushdeer.os.ui.compose.componment.SwipeToDismissItem
import com.pushdeer.os.ui.navigation.Page
import com.pushdeer.os.util.SystemUtil


@ExperimentalMaterialApi
@Composable
fun DeviceListPage(requestHolder: RequestHolder) {
    MainPageFrame(
        titleStringId = Page.Devices.labelStringId,
        onSideIconClick = {
            requestHolder.deviceReg(
                deviceInfo = DeviceInfo().apply {
                    name = System.currentTimeMillis().toString()
                    device_id = "sdsdf"
                    is_clip = 0
                }
            )
        }
    ) {
        val state = rememberLazyListState()
        LazyColumn(state = state) {
            items(
                items = requestHolder.pushDeerViewModel.deviceList,
                key = { item: DeviceInfo -> item.id }) { deviceInfo: DeviceInfo ->
                SwipeToDismissItem(onDismiss = { requestHolder.deviceRemove(deviceInfo) }) {
                    CardItemSingleLineWithIcon(
                        onClick = {},
                        resId = R.drawable.ipad_landscape2x,
                        text = if (deviceInfo.device_id == requestHolder.settingStore.thisDeviceId) "${deviceInfo.name} (this device)" else deviceInfo.name
                    )
                }
            }
            item {
                ListBottomBlankItem()
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun DeviceListPage(userInfo: UserInfo, token: String, regid: String) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .padding(top = 8.dp)
    ) {
        item {
            Card(elevation = 5.dp, onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                    Text(text = "当前版本 Android ${SystemUtil.getSystemVersion()}")
                    Text(text = "本机品牌 ${SystemUtil.getDeviceBrand()}")
                    Text(text = "本机型号 ${SystemUtil.getDeviceModel()}")
                    Text(text = "产品名称 ${Build.PRODUCT}")
                    MiuiVersion()
                }
            }
        }
        item {
            Card(elevation = 5.dp, onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                    Text(text = "id ${userInfo.id}")
                    Text(text = "name ${userInfo.name}")
                    Text(text = "email ${userInfo.email}")
                    Text(text = "app_id ${userInfo.app_id}")
                    Text(text = "wechat_id ${userInfo.wechat_id}")
                    Text(text = "created_at ${userInfo.created_at}")
                    Text(text = "updated_at ${userInfo.updated_at}")
                    Text(text = "level ${userInfo.level}")
                    Text(text = "token $token")
                    Text(text = "regid $regid")
                }
            }
        }
    }
}

@Composable
fun MiuiVersion() {
    val v = SystemUtil.getMiuiVersion()
    if (!TextUtils.isEmpty(v)) {
        Text(text = "Miui 版本 ${v}")
    }
}