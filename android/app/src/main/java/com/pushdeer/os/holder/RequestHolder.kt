package com.pushdeer.os.holder

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.res.Resources
import android.util.Log
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
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.wh.common.activity.QrScanActivity
import com.willowtreeapps.signinwithapplebutton.SignInWithAppleConfiguration
import com.willowtreeapps.signinwithapplebutton.SignInWithAppleResult
import com.willowtreeapps.signinwithapplebutton.SignInWithAppleService
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
    val requestPermissionOpener: ActivityResultLauncher<Array<String>>
    val coilImageLoader: ImageLoader


    // requests
    val alert: AlertRequest
    val key: KeyRequest
    val device: DeviceRequest
    val message: MessageRequest
    val clip: ClipRequest
    val weChatLogin: WeChatLoginRequest
    val appleLogin: AppleLoginRequest

//    val iwxapi: IWXAPI

    fun startQrScanActivity() {
        qrScanActivityOpener.launch(QrScanActivity.forScanResultIntent(myActivity))
    }

    fun toggleMessageSender() {
        settingStore.showMessageSender = settingStore.showMessageSender.not()
        uiViewModel.showMessageSender = settingStore.showMessageSender
    }

    fun clearLogDog() {
        alert.alert(R.string.global_alert_title_confirm, "Clear?", onOk = {
            logDogViewModel.clear()
        })
    }

    class AppleLoginRequest(
        private val fragmentManager: FragmentManager,
        private val requestHolder: RequestHolder
    ) {
        private val appleLoginCallBack: (SignInWithAppleResult) -> Unit =
            { result: SignInWithAppleResult ->
                when (result) {
                    is SignInWithAppleResult.Success -> {
                        if (requestHolder.pushDeerViewModel.userInfo.isWeChatLogin) {
                            // if login with wechat, perform merge
                            requestHolder.coroutineScope.launch {
                                requestHolder.pushDeerViewModel.userMerge(
                                    type = "apple",
                                    tokenorcode = result.idToken,
                                    onReturn = {
                                        requestHolder.coroutineScope.launch {
                                            requestHolder.pushDeerViewModel.userInfo()
                                        }
                                    }
                                )
                            }
                        } else {
                            // else ( is not login ), plain login with apple
                            requestHolder.coroutineScope.launch {
                                requestHolder.pushDeerViewModel.loginWithApple(result.idToken) {
                                    requestHolder.globalNavController.navigate("main") {
                                        requestHolder.globalNavController.popBackStack()
                                    }
                                }
                            }
                        }
                    }
                    is SignInWithAppleResult.Failure -> {
                        requestHolder.alert.alert("Warning Apple Id Login Failed", {
                            result.error.message
                        }, onOk = {})
                        Log.d(
                            "WH_",
                            "Received error from Apple Sign In ${result.error.message}"
                        )
                    }
                    is SignInWithAppleResult.Cancel -> {
                        Log.d("WH_", "User canceled Apple Sign In")
                    }
                }
            }

        private val appleLoginConfiguration = SignInWithAppleConfiguration.Builder()
            .clientId("com.pushdeer.site")
            .redirectUri("https://api2.pushdeer.com/callback/apple")
            .responseType(SignInWithAppleConfiguration.ResponseType.ALL)
            .scope(SignInWithAppleConfiguration.Scope.EMAIL)
            .build()

        val login = {
            val service = SignInWithAppleService(
                fragmentManager = fragmentManager,
                fragmentTag = "SignInWithAppleButton-1-SignInWebViewDialogFragment",
                configuration = appleLoginConfiguration,
                callback = appleLoginCallBack
            )
            service.show()
        }
    }

    class WeChatLoginRequest(val iwxapi: IWXAPI) {
        val login: () -> Unit = {
            val req = SendAuth.Req()
            req.scope = "snsapi_userinfo"
            req.state = System.currentTimeMillis().toString()
            iwxapi.sendReq(req)
        }
    }

    class ClipRequest(private val clipboardManager: ClipboardManager) {
        fun copyMessagePlainText(str: String) {
            clipboardManager.setPrimaryClip(ClipData.newPlainText("pushdeer-copy-plain-text", str))
        }

        fun copyPushKey(str: String) {
            clipboardManager.setPrimaryClip(ClipData.newPlainText("pushdeer-copy-pushkey", str))
        }
    }

    class AlertRequest(private val resources: Resources) {
        val show2BtnDialog: MutableState<Boolean> = mutableStateOf(false)
        val show1BtnDialog: MutableState<Boolean> = mutableStateOf(false)
        var title: String = ""
        var content: @Composable () -> Unit = {}
        var onOKAction: () -> Unit = {}
        var onCancelAction: () -> Unit = {}


        fun alert(
            title: String,
            content: @Composable () -> Unit,
            onOk: () -> Unit,
        ) {
            this.title = title
            this.content = content
            this.onOKAction = onOk
            this.show1BtnDialog.value = true
        }

        fun alert(
            title: String,
            content: @Composable () -> Unit,
            onOk: () -> Unit,
            onCancel: () -> Unit
        ) {
            this.title = title
            this.content = content
            this.onOKAction = onOk
            this.onCancelAction = onCancel
            this.show2BtnDialog.value = true
        }

        fun alert(title: String, content: String, onOk: () -> Unit, onCancel: () -> Unit = {}) {
            alert(title, { Text(text = content) }, onOk, onCancel)
        }

        fun alert(title: String, content: String, onOk: () -> Unit) {
            alert(title, { Text(text = content) }, onOk)
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
            @StringRes content: Int,
            onOk: () -> Unit = {},
        ) {
            alert(resources.getString(title), resources.getString(content), onOk)
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
            content: @Composable () -> Unit,
            onOk: () -> Unit,
        ) {
            alert(resources.getString(title), content, onOk)
        }

        fun alert(
            @StringRes title: Int,
            content: String,
            onOk: () -> Unit,
            onCancel: () -> Unit = {}
        ) {
            alert(resources.getString(title), content, onOk, onCancel)
        }

        fun alert(
            @StringRes title: Int,
            content: String,
            onOk: () -> Unit,
        ) {
            alert(resources.getString(title), content, onOk)
        }
    }

    class KeyRequest(private val requestHolder: RequestHolder) {
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

    class DeviceRequest(private val requestHolder: RequestHolder) {
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

    class MessageRequest(private val requestHolder: RequestHolder) {
        fun messagePush(text: String, desp: String, type: String, pushkey: String) {
            requestHolder.coroutineScope.launch {
                requestHolder.pushDeerViewModel.messagePush(text, desp, type, pushkey)
            }
        }

        fun messagePushTest(text: String) {
            if (requestHolder.pushDeerViewModel.keyList.isNotEmpty()) {
                messagePush(
                    text,
                    "pushtest",
                    "markdown",
                    requestHolder.pushDeerViewModel.keyList[0].key
                )
                requestHolder.coroutineScope.launch {
                    delay(900)
                    requestHolder.pushDeerViewModel.messageList()
                }
            } else {
                requestHolder.alert.alert(
                    R.string.global_alert_title_alert,
                    R.string.main_message_send_alert
                )
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