package com.wh.common.toy.qweather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wh.common.toy.qweather.data.QWeather
import com.wh.common.toy.qweather.helper.QWeatherHttpApiHelper
import com.wh.common.toy.qweather.store.QWeatherStore
import kotlinx.coroutines.launch

class QWeatherViewModel(qWeatherStore: QWeatherStore) : ViewModel() {

    var qWeather = MutableLiveData<QWeather>()

    fun refreshQWeatherAsync() {
        viewModelScope.launch {
            qWeather.postValue(QWeatherHttpApiHelper.fetchRealTimeWeather())
        }
    }
}