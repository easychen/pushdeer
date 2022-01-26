package com.pushdeer.os.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.pushdeer.os.data.api.PushDeerApi
import com.pushdeer.os.data.api.data.request.DeviceInfo
import com.pushdeer.os.data.api.data.response.PushKey
import com.pushdeer.os.data.api.data.response.UserInfo
import com.pushdeer.os.data.repository.LogDogRepository
import com.pushdeer.os.data.repository.MessageRepository
import com.pushdeer.os.store.SettingStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PushDeerViewModel(
    private val settingStore: SettingStore,
    private val logDogRepository: LogDogRepository,
    private val pushDeerService: PushDeerApi,
    private val messageRepository: MessageRepository
) : ViewModel() {
    private val TAG = "WH_"

    var token: String by mutableStateOf(settingStore.userToken)
    var userInfo: UserInfo by mutableStateOf(UserInfo())
    var deviceList = mutableStateListOf<DeviceInfo>()
    val keyList = mutableStateListOf<PushKey>()
//    var messageList = mutableStateListOf<Message>()

    suspend fun loginWithApple(idToken: String = "", onReturn: (String) -> Unit = {}) {
        withContext(Dispatchers.IO) {
            if (token == "" && idToken != "") {
                try {
                    pushDeerService.loginWithAppleIdToken(idToken).let {
                        it.content?.let { tokenOnly ->
                            settingStore.userToken = tokenOnly.token
                            token = tokenOnly.token
                            Log.d(TAG, "loginWithApple: $token")
                            withContext(Dispatchers.Main) {
                                onReturn.invoke(token)
                            }
                        }
                    }
                    logDogRepository.logi("loginWithApple", "withAppleId", "nothing happened")
                } catch (e: Exception) {
                    Log.d(TAG, "loginWithApple: ${e.localizedMessage}")
                    logDogRepository.loge("loginWithApple", "", e.toString())
                }
            } else if (token == "" && idToken == "") {
                return@withContext
            } else if (token != "") {
                withContext(Dispatchers.Main) {
                    onReturn.invoke(token)
                }
            }
        }
    }

    suspend fun loginWithWeiXin(code:String,onReturn: (String) -> Unit){
        withContext(Dispatchers.IO){
            try {
                pushDeerService.loginWithWeXin(code).let {
                    it.content?.let { tokenOnly ->
                        settingStore.userToken = tokenOnly.token
                        token = tokenOnly.token
                        Log.d(TAG, "loginWithWeiXin: $token")
                        withContext(Dispatchers.Main) {
                            onReturn.invoke(token)
                        }
                    }
                }
                logDogRepository.logi("loginWithWeiXin", "withWeiXin", "nothing happened")
            }catch (e: Exception){
                Log.e(TAG, "loginWithWeiXin: ${e.localizedMessage}")
                logDogRepository.loge("loginWithWeiXin", "", e.toString())
            }
        }
    }

    suspend fun userMerge(type:String,tokenorcode:String,onReturn: (String) -> Unit={}){
        Log.d("WH_", ": token:${token} type:${type} tokenorcode:${tokenorcode}")

        withContext(Dispatchers.IO){
            try {
                pushDeerService.userMerge(token,type,tokenorcode).let {
                    Log.d(TAG, "userMerge: $it")
                    withContext(Dispatchers.Main){
                        onReturn("")
                    }
                }
            }catch (e: Exception) {
                Log.e(TAG, "userMerge:e ${e.localizedMessage}")
                logDogRepository.loge("userMerge", "", e.toString())
            }
        }
    }

    suspend fun userInfo(onOk: (UserInfo) -> Unit = {}, onFailed: () -> Unit = {}) {
        withContext(Dispatchers.IO) {
            try {
                pushDeerService.userInfo(token).let {
                    Log.d(TAG, "userInfo: ${it.content}")
                    it.content?.let { ita ->
                        userInfo = ita
                        withContext(Dispatchers.Main) {
                            onOk(userInfo)
                        }
                    }
                }
                logDogRepository.logi("userInfo", "normally", "nothing happened")
            } catch (e: Exception) {
                Log.d(TAG, "userInfo: ${e.localizedMessage}")
                logDogRepository.loge("userInfo", "", e.toString())
            }
        }
    }

    suspend fun deviceReg(deviceInfo: DeviceInfo, onReturn: (DeviceInfo) -> Unit = {}) {
        withContext(Dispatchers.IO) {
            try {
                pushDeerService.deviceReg(deviceInfo.toRequestMap(token)).let {
                    it.content?.let {
                        deviceList.clear()
                        deviceList.addAll(it.devices)
                        deviceList.filter {
                            it.device_id == deviceInfo.device_id
                        }.let { dis ->
                            if (dis.isNotEmpty()) {
                                withContext(Dispatchers.Default) {
                                    onReturn(dis.first())
                                }
                            }
                        }
                    }
                }
                logDogRepository.logi("deviceReg", "normally", "nothing happened")
            } catch (e: Exception) {
                Log.d(TAG, "deviceReg: ${e.localizedMessage}")
                logDogRepository.loge("deviceReg", "", e.toString())
            }
        }
    }

    suspend fun deviceList(onReturn: (List<DeviceInfo>) -> Unit = {}) {
        withContext(Dispatchers.IO) {
            try {
                pushDeerService.deviceList(token).let {
                    it.content?.let {
                        deviceList.clear()
                        deviceList.addAll(it.devices.reversed())
                    }
                }
                logDogRepository.logi("deviceList", "normally", "nothing happened")
            } catch (e: Exception) {
                Log.d(TAG, "deviceList: ${e.localizedMessage}")
                logDogRepository.loge("deviceList", "", e.toString())
            }
        }
    }

    fun shouldRegDevice(): Boolean {
        return deviceList.none { it.device_id == settingStore.thisDeviceId }
    }

    suspend fun deviceRemove(deviceId: Int) {
        withContext(Dispatchers.IO) {
            try {
                pushDeerService.deviceRemove(token, deviceId).let {
                    deviceList()
                    Log.d(TAG, "deviceRemove: $it")
                }
                logDogRepository.logi("deviceRemove", "normally", "nothing happened")
            } catch (e: Exception) {
                Log.d(TAG, "deviceRemove: ${e.localizedMessage}")
                logDogRepository.loge("deviceRemove", "", e.toString())
            }
        }
    }

    suspend fun deviceRename(deviceInfo: DeviceInfo, onReturn: () -> Unit = {}) {
        withContext(Dispatchers.IO) {
            try {
                pushDeerService.deviceRename(token, deviceInfo.id, deviceInfo.name)
                logDogRepository.logi("deviceRename", "normally", "nothing happened")
                onReturn()
            } catch (e: Exception) {
                Log.d(TAG, "deviceRename: ${e.localizedMessage}")
                logDogRepository.loge("deviceRename", "", e.toString())
            }
        }
    }

    suspend fun keyGen() {
        withContext(Dispatchers.IO) {
            try {
                pushDeerService.keyGen(token).let {
                    it.content?.let {
                        keyList.clear()
                        keyList.addAll(it.keys)
                    }
                }
                logDogRepository.logi("keyGen", "normally", "nothing happened")
            } catch (e: Exception) {
                Log.d(TAG, "keyGen: ${e.localizedMessage}")
                logDogRepository.loge("keyGen", "", e.toString())
            }
        }
    }

    suspend fun keyRegen(keyId: String) {
        withContext(Dispatchers.IO) {
            try {
                pushDeerService.keyRegen(
                    mapOf(
                        "token" to token,
                        "id" to keyId
                    )
                ).let {
                    keyList()
                }
                logDogRepository.logi("keyRegen", "normally", "nothing happened")
            } catch (e: Exception) {
                Log.d(TAG, "keyRegen: ${e.localizedMessage}")
                logDogRepository.loge("keyRegen", "", e.toString())
            }
        }
    }

    suspend fun keyRename(key: PushKey, onReturn: () -> Unit = {}) {
        withContext(Dispatchers.IO) {
            try {
                pushDeerService.keyRename(
                    token,
                    key.id,
                    key.name
                )
                logDogRepository.logi("keyRename", "normally", "nothing happened")
                onReturn()
            } catch (e: Exception) {
                Log.d(TAG, "keyRename: ${e.localizedMessage}")
                logDogRepository.loge("keyRename", "", e.toString())
            }
        }
    }

    suspend fun keyList() {
        withContext(Dispatchers.IO) {
            try {
                pushDeerService.keyList(token).let {
                    it.content?.let {
                        keyList.clear()
                        keyList.addAll(it.keys)
                    }
                }
                logDogRepository.logi("keyList", "normally", "nothing happened")
            } catch (e: Exception) {
                Log.d(TAG, "keyList: ${e.localizedMessage}")
                logDogRepository.loge("keyList", "", e.toString())
            }
        }
    }

    suspend fun keyRemove(keyId: String) {
        withContext(Dispatchers.IO) {
            try {
                pushDeerService.keyRemove(mapOf("token" to token, "id" to keyId)).let {
                    keyList()
                }
                logDogRepository.logi("keyRemove", "normally", "nothing happened")
            } catch (e: Exception) {
                Log.d(TAG, "keyRemove: ${e.localizedMessage}")
                logDogRepository.loge("keyRemove", "", e.toString())
            }
        }

    }

    suspend fun messagePush(
        text: String,
        desp: String,
        type: String = "markdown",
        pushkey: String
    ) {
        withContext(Dispatchers.IO) {
            try {
                pushDeerService.messagePush(
                    mapOf(
                        "pushkey" to pushkey,
                        "text" to text,
                        "desp" to desp,
                        "type" to type
                    )
                ).let {
                    messageList()
                }
                logDogRepository.logi("messagePush", "normally", "nothing happened")
            } catch (e: Exception) {
                Log.d(TAG, "messagePush: ${e.localizedMessage}")
            }
        }
    }

    suspend fun messageList() {
        withContext(Dispatchers.IO) {
            try {
                pushDeerService.messageList(token).let {
                    it.content?.let {
                        messageRepository.insert(*(it.messages.toTypedArray()))
                    }
                }
                logDogRepository.logi("messageList", "normally", "nothing happened")
            } catch (e: Exception) {
                Log.d(TAG, "messageList: ${e.localizedMessage}")
            }
        }
    }

    suspend fun messageRemove(messageId: Int) {
        withContext(Dispatchers.IO) {
            try {
                pushDeerService.messageRemove(token, messageId).let {
                    messageList()
                }
                logDogRepository.logi("messageRemove", "normally", "nothing happened")
            } catch (e: Exception) {
                Log.d(TAG, "keyRemove: ${e.localizedMessage}")
            }
        }
    }
}