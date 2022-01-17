package com.pushdeer.os.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.pushdeer.os.store.SettingStore

class UiViewModel(private val settingStore: SettingStore): ViewModel() {
    var showMessageSender by mutableStateOf(settingStore.showMessageSender)
}