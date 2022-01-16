package com.pushdeer.os.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pushdeer.os.data.api.PushDeerApi
import com.pushdeer.os.data.api.data.response.Message
import com.pushdeer.os.data.database.entity.MessageEntity
import com.pushdeer.os.data.repository.MessageRepository
import com.pushdeer.os.store.SettingStore
import kotlinx.coroutines.launch


class MessageViewModel(
    private val messageRepository: MessageRepository,
    private val settingStore: SettingStore,
    private val pushDeerService: PushDeerApi
) : ViewModel() {
    val all = messageRepository.all

    fun insert(vararg message: Message) {
        viewModelScope.launch {
            messageRepository.insert(*message)
        }
    }

    fun delete(vararg message: Message) {
        viewModelScope.launch {
            messageRepository.delete(*message)
        }
    }

    fun delete(vararg messageEntity: MessageEntity){
        viewModelScope.launch {
            messageRepository.delete(*messageEntity)
        }
    }
}