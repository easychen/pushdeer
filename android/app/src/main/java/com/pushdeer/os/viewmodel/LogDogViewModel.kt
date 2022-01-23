package com.pushdeer.os.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pushdeer.os.data.repository.LogDogRepository
import kotlinx.coroutines.launch

class LogDogViewModel(private val logDogRepository: LogDogRepository): ViewModel() {
    val all = logDogRepository.all

//    suspend fun clear(){
//        logDogRepository.clear()
//    }

    fun clear(){
        viewModelScope.launch {
            logDogRepository.clear()
        }
    }
}