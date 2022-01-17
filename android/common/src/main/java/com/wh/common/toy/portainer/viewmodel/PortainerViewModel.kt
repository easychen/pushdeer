package com.wh.common.toy.portainer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wh.common.toy.portainer.repository.PortainerRepository
import kotlinx.coroutines.launch

class PortainerViewModel(private val portainerRepository: PortainerRepository) : ViewModel() {

    val portainers = portainerRepository.portainers

    var isLoading = portainerRepository.isLoading

    var lastUpdateTimestamp = portainerRepository.lastUpdateTimestamp

    fun refreshContainersAsync() {
        viewModelScope.launch {
            try {
                portainerRepository.refreshContainers()
            } catch (e: Exception) {
                Log.d("WH_", "refreshContainersAsync: ${e.localizedMessage}")
            }
        }
    }
}