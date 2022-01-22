package com.pushdeer.os.holder

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.res.Resources
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavHostController
import coil.ImageLoader
import com.pushdeer.os.R
import com.pushdeer.os.data.api.data.request.DeviceInfo
import com.pushdeer.os.data.api.data.response.Message
import com.pushdeer.os.data.api.data.response.PushKey
import com.pushdeer.os.store.SettingStore
import com.pushdeer.os.viewmodel.LogDogViewModel
import com.pushdeer.os.viewmodel.MessageViewModel
import com.pushdeer.os.viewmodel.PushDeerViewModel
import com.pushdeer.os.viewmodel.UiViewModel
import com.wh.common.activity.QrScanActivity
import io.noties.markwon.Markwon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

interface RequestHolder {
    val uiViewModel: UiViewModel
    val pushDeerViewModel: PushDeerViewModel
    val logDogViewModel: LogDogViewModel
    val messageViewModel: MessageViewModel
    val settingStore: SettingStore
    val globalNavController: NavHostController
    val coroutineScope: CoroutineScope
    val myActivity: AppCompatActivity
    val markdown: Markwon
    val qrScanActivityOpener: ActivityResultLauncher<Intent>
    val requestPermissionOpener:ActivityResultLauncher<Array<String>>
    val coilImageLoader: ImageLoader

    val fragmentManager: FragmentManager

    val alert: AlertRequest
    val key:KeyRequest
    val device:DeviceRequest
    val message:MessageRequest
    val clip:ClipRequest

    fun startQrScanActivity() {
        qrScanActivityOpener.launch(QrScanActivity.forScanResultIntent(myActivity))
    }

    fun toggleMessageSender() {
        settingStore.showMessageSender = settingStore.showMessageSender.not()
        uiViewModel.showMessageSender = settingStore.showMessageSender
    }

    fun clearLogDog() {
        alert.alert(R.string.global_alert_title_confirm,"Clear?",onOk = {
            coroutineScope.launch {
                logDogViewModel.clear()
            }
        })
    }

    abstract class ClipRequest(private val clipboardManager: ClipboardManager) {
        fun copyMessagePlainText(str: String) {
            clipboardManager.setPrimaryClip(ClipData.newPlainText("pushdeer-copy-plain-text", str))
        }

        fun copyPushKey(str: String){
            clipboardManager.setPrimaryClip(ClipData.newPlainText("pushdeer-copy-pushkey", str))
        }
    }

    abstract class AlertRequest(private val resources: Resources) {
        val show: MutableState<Boolean> = mutableStateOf(false)
        var title: String = ""
        var content: @Composable () -> Unit = {}
        var onOKAction: () -> Unit = {}
        var onCancelAction: () -> Unit = {}


        fun alert(
            title: String,
            content: @Composable () -> Unit,
            onOk: () -> Unit,
            onCancel: () -> Unit = {}
        ) {
            this.title = title
            this.content = content
            this.onOKAction = onOk
            this.onCancelAction = onCancel
            this.show.value = true
        }

        fun alert(title: String, content: String, onOk: () -> Unit, onCancel: () -> Unit = {}) {
            alert(title, { Text(text = content) }, onOk, onCancel)
        }

        fun alert(
            @StringRes title: Int,
            @StringRes content: Int,
            onOk: () -> Unit,
            onCancel: () -> Unit = {}
        ) {
            alert(resources.getString(title), resources.getString(content), onOk, onCancel)
        }

        fun alert(
            @StringRes title: Int,
            content: @Composable () -> Unit,
            onOk: () -> Unit,
            onCancel: () -> Unit = {}
        ) {
            alert(resources.getString(title), content, onOk, onCancel)
        }

        fun alert(
            @StringRes title: Int,
            content: String,
            onOk: () -> Unit,
            onCancel: () -> Unit = {}
        ) {
            alert(resources.getString(title), content, onOk, onCancel)
        }
    }

    abstract class KeyRequest(private val requestHolder: RequestHolder){
        fun gen() {
            requestHolder.coroutineScope.launch {
                requestHolder.pushDeerViewModel.keyGen()
            }
        }

        fun regen(keyId: String) {
            requestHolder.coroutineScope.launch {
                requestHolder.pushDeerViewModel.keyRegen(keyId)
            }
        }

        fun remove(pushKey: PushKey) {
            requestHolder.coroutineScope.launch {
                requestHolder.pushDeerViewModel.keyList.remove(pushKey)
                requestHolder.pushDeerViewModel.keyRemove(pushKey.id)
            }
        }

        fun rename(pushKey: PushKey) {
            requestHolder.coroutineScope.launch {
                requestHolder.pushDeerViewModel.keyRename(pushKey) {
                    requestHolder.coroutineScope.launch {
                        requestHolder.pushDeerViewModel.keyList()
                    }
                }
            }
        }
    }

    abstract class DeviceRequest(private val requestHolder: RequestHolder){
        fun deviceReg(deviceInfo: DeviceInfo) {
            requestHolder.coroutineScope.launch {
                requestHolder.pushDeerViewModel.deviceReg(deviceInfo)
            }
        }

        fun deviceRemove(deviceInfo: DeviceInfo) {
            requestHolder.coroutineScope.launch {
                requestHolder.pushDeerViewModel.deviceList.remove(deviceInfo)
                requestHolder.pushDeerViewModel.deviceRemove(deviceInfo.id)
            }
        }

        fun deviceRename(deviceInfo: DeviceInfo) {
            requestHolder.coroutineScope.launch {
                requestHolder.pushDeerViewModel.deviceRename(deviceInfo) {
                    requestHolder.coroutineScope.launch {
                        requestHolder.pushDeerViewModel.deviceList()
                    }
                }
            }
        }
    }

    abstract class MessageRequest(private val requestHolder: RequestHolder){
        fun messagePush(text: String, desp: String, type: String, pushkey: String) {
            requestHolder.coroutineScope.launch {
                requestHolder.pushDeerViewModel.messagePush(text, desp, type, pushkey)
            }
        }

        fun messagePushTest(text: String) {
            if (requestHolder.pushDeerViewModel.keyList.isNotEmpty()) {
                messagePush(text, "pushtest", "markdown", requestHolder.pushDeerViewModel.keyList[0].key)
                requestHolder.coroutineScope.launch {
                    delay(1000)
                    requestHolder.pushDeerViewModel.messageList()
                }
            } else {
                requestHolder.alert.alert(
                    R.string.global_alert_title_alert,
                    R.string.main_message_send_alert,
                    onOk = {})
            }
        }

        fun messageRemove(message: Message, onDone: () -> Unit = {}) {
            requestHolder.coroutineScope.launch {
//            pushDeerViewModel.messageList.remove(message)
                requestHolder.pushDeerViewModel.messageRemove(message.id)
                onDone()
            }
        }
    }
}