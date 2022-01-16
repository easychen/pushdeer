package com.pushdeer.os.holder

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.navigation.NavHostController
import coil.ImageLoader
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
    val messageViewModel:MessageViewModel
    val settingStore: SettingStore
    val globalNavController: NavHostController
    val coroutineScope: CoroutineScope
    val myActivity: ComponentActivity
    val markdown: Markwon
    val activityOpener: ActivityResultLauncher<Intent>
    val coilImageLoader: ImageLoader

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

    fun deviceReg(deviceInfo: DeviceInfo){
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

    fun messagePush(text: String, desp: String, type: String, pushkey: String) {
        coroutineScope.launch {
            pushDeerViewModel.messagePush(text, desp, type, pushkey)
        }
    }

    fun messagePushTest(desp: String) {
        if (pushDeerViewModel.keyList.isNotEmpty()) {
            messagePush("pushtest", desp, "markdown", pushDeerViewModel.keyList[0].key)
            coroutineScope.launch {
                delay(1000)
                pushDeerViewModel.messageList()
            }
        } else
            Log.d("WH_", "messagePushTest: keylist is empty")
    }

    fun messageRemove(message: Message,onDone:()->Unit={}) {
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
}