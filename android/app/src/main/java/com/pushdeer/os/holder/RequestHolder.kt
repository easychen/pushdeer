package com.pushdeer.os.holder

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.res.Resources
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.StringRes
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavHostController
import coil.ImageLoader
import com.pushdeer.os.R
import com.pushdeer.os.activity.QrScanActivity
import com.pushdeer.os.data.api.data.request.DeviceInfo
import com.pushdeer.os.data.api.data.response.Message
import com.pushdeer.os.data.api.data.response.PushKey
import com.pushdeer.os.store.SettingStore
import com.pushdeer.os.viewmodel.LogDogViewModel
import com.pushdeer.os.viewmodel.MessageViewModel
import com.pushdeer.os.viewmodel.PushDeerViewModel
import com.pushdeer.os.viewmodel.UiViewModel
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
    val myActivity: ComponentActivity
    val markdown: Markwon
    val activityOpener: ActivityResultLauncher<Intent>
    val coilImageLoader: ImageLoader

//    val resource:Resources

    val fragmentManager: FragmentManager

    val alert: AlertRequest

    val clipboardManager: ClipboardManager

    fun copyPlainString(str: String) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText("pushdeer-pushkey", str))
    }

    fun startQrScanActivity() {
        activityOpener.launch(QrScanActivity.forScanResultIntent(myActivity))
    }

    fun keyGen() {
        coroutineScope.launch {
            pushDeerViewModel.keyGen()
        }
    }

    fun keyRegen(keyId: String) {
        coroutineScope.launch {
            pushDeerViewModel.keyRegen(keyId)
        }
    }

    fun keyRemove(pushKey: PushKey) {
        coroutineScope.launch {
            pushDeerViewModel.keyList.remove(pushKey)
            pushDeerViewModel.keyRemove(pushKey.id)
        }
    }

    fun keyRename(pushKey: PushKey){
        coroutineScope.launch {
            pushDeerViewModel.keyRename(pushKey){
                coroutineScope.launch {
                    pushDeerViewModel.keyList()
                }
            }
        }
    }

    fun deviceReg(deviceInfo: DeviceInfo) {
        coroutineScope.launch {
            pushDeerViewModel.deviceReg(deviceInfo)
        }
    }

    fun deviceRemove(deviceInfo: DeviceInfo) {
        coroutineScope.launch {
            pushDeerViewModel.deviceList.remove(deviceInfo)
            pushDeerViewModel.deviceRemove(deviceInfo.id)
        }
    }

    fun deviceRename(deviceInfo: DeviceInfo) {
        coroutineScope.launch {
            pushDeerViewModel.deviceRename(deviceInfo) {
                coroutineScope.launch {
                    pushDeerViewModel.deviceList()
                }
            }
        }
    }

    fun messagePush(text: String, desp: String, type: String, pushkey: String) {
        coroutineScope.launch {
            pushDeerViewModel.messagePush(text, desp, type, pushkey)
        }
    }

    fun messagePushTest(text: String) {
        if (pushDeerViewModel.keyList.isNotEmpty()) {
            messagePush(text, "pushtest", "markdown", pushDeerViewModel.keyList[0].key)
            coroutineScope.launch {
                delay(1000)
                pushDeerViewModel.messageList()
            }
        } else {
            alert.alert(
                R.string.global_alert_title_alert,
                R.string.main_message_send_alert,
                onOk = {})
        }
    }

    fun messageRemove(message: Message, onDone: () -> Unit = {}) {
        coroutineScope.launch {
//            pushDeerViewModel.messageList.remove(message)
            pushDeerViewModel.messageRemove(message.id)
            onDone()
        }
    }

    fun toggleMessageSender() {
        settingStore.showMessageSender = settingStore.showMessageSender.not()
        uiViewModel.showMessageSender = settingStore.showMessageSender
    }

    fun clearLogDog() {
        coroutineScope.launch {
            logDogViewModel.clear()
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
            onCancel: () -> Unit={}
        ) {
            alert(resources.getString(title), content, onOk, onCancel)
        }
    }
}