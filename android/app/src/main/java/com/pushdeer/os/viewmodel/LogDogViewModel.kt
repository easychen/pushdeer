package com.pushdeer.os.viewmodel

import androidx.lifecycle.ViewModel
import com.pushdeer.os.data.repository.LogDogRepository

class LogDogViewModel(private val logDogRepository: LogDogRepository): ViewModel() {
    val all = logDogRepository.all

    suspend fun clear(){
        logDogRepository.clear()
    }
}