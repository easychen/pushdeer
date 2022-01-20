package com.pushdeer.os.ui.compose.page.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pushdeer.os.R
import com.pushdeer.os.data.api.data.request.DeviceInfo
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
            if (requestHolder.settingStore.thisDeviceId == "") {
                requestHolder.alert.alert(
                    title = "Confirm",
                    content = "This Device Registered Failed in PushSDK",
                    onOk = {})
                // device regid got failed
            } else {
                requestHolder.deviceReg(
                    deviceInfo = DeviceInfo().apply {
                        name = SystemUtil.getDeviceModel()
                        device_id = requestHolder.settingStore.thisDeviceId
                        is_clip = 0
                    }
                )
            }
        },
        showSideIcon = requestHolder.pushDeerViewModel.shouldRegDevice()
    ) {
        if (requestHolder.pushDeerViewModel.deviceList.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "It's Empty, Click '+' to Add New Device"
                )
            }
        } else {
            val state = rememberLazyListState()
            LazyColumn(state = state) {
                items(
                    items = requestHolder.pushDeerViewModel.deviceList,
                    key = { item: DeviceInfo -> item.id }) { deviceInfo: DeviceInfo ->
                    SwipeToDismissItem(
                        onAction = { requestHolder.deviceRemove(deviceInfo) }
                    ) {
                        CardItemSingleLineWithIcon(
                            onClick = {},
                            resId = R.drawable.ipad_landscape2x,
                            text = if (deviceInfo.device_id == requestHolder.settingStore.thisDeviceId) "${deviceInfo.name} (this device) " else deviceInfo.name
                        )
                    }
                }
                item {
                    ListBottomBlankItem()
                }
            }
        }
    }
}