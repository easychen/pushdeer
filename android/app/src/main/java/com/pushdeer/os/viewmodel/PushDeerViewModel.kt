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
    private val pushDeerService:PushDeerApi,
    private val messageRepository: MessageRepository
) : ViewModel() {
    private val TAG = "WH_"

    var token: String by mutableStateOf(settingStore.userToken)
    var userInfo: UserInfo by mutableStateOf(UserInfo())
    var deviceList = mutableStateListOf<DeviceInfo>()
    val keyList = mutableStateListOf<PushKey>()
//    var messageList = mutableStateListOf<Message>()

    suspend fun login(onReturn: (String) -> Unit = {}) {
        withContext(Dispatchers.IO) {
            if (token == "") {
                try {
                    pushDeerService.fakeLogin().let {
                        it.content?.let { tokenOnly ->
                            settingStore.userToken = tokenOnly.token
                            token = tokenOnly.token
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "login: ${e.localizedMessage}")
                    logDogRepository.loge("login", "", e.toString())
                    return@withContext
                }
                logDogRepository.logi("login","normally","nothing happened")
            }
//            Log.d(TAG, "login: token $token")
        }
    }

    suspend fun userInfo(onReturn: (UserInfo) -> Unit = {}) {
        withContext(Dispatchers.IO) {
            try {
                pushDeerService.userInfo(token).let {
                    it.content?.let { ita ->
                        userInfo = ita
                    }
                }
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
                        deviceList.addAll(it.devices)
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "deviceList: ${e.localizedMessage}")
            }
        }
    }

    fun shouldRegDevice(): Boolean {
//        Log.d(TAG, "isDeviceReged: current device id ${settingStore.thisDeviceId}")
        return deviceList.none { it.device_id == settingStore.thisDeviceId }
    }

    suspend fun deviceRemove(deviceId: Int) {
        withContext(Dispatchers.IO) {
            try {
                pushDeerService.deviceRemove(token, deviceId).let {
                    deviceList()
                    Log.d(TAG, "deviceRemove: $it")
                }
            } catch (e: Exception) {
                Log.d(TAG, "deviceRemove: ${e.localizedMessage}")
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
            } catch (e: Exception) {
                Log.d(TAG, "keyGen: ${e.localizedMessage}")
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
            } catch (e: Exception) {
                Log.d(TAG, "keyRegen: ${e.localizedMessage}")
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
            } catch (e: Exception) {
                Log.d(TAG, "keyList: ${e.localizedMessage}")
            }
        }
    }

    suspend fun keyRemove(keyId: String) {
        withContext(Dispatchers.IO) {
            try {
                pushDeerService.keyRemove(mapOf("token" to token, "id" to keyId)).let {
                    keyList()
                }
            } catch (e: Exception) {
                Log.d(TAG, "keyRemove: ${e.localizedMessage}")
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
            } catch (e: Exception) {
                Log.d(TAG, "keyRemove: ${e.localizedMessage}")
            }
        }
    }
}