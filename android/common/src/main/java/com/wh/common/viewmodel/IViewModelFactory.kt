package com.wh.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class IViewModelFactory : ViewModelProvider.Factory {

    abstract override fun <T : ViewModel> create(modelClass: Class<T>): T

//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        when {
//            modelClass.isAssignableFrom(PortainerViewModel::class.java) -> {
//                @Suppress("UNCHECKED_CAST")
//                return PortainerViewModel(portainerStore) as T
//            }
//            modelClass.isAssignableFrom(QWeatherViewModel::class.java) -> {
//                @Suppress("UNCHECKED_CAST")
//                return QWeatherViewModel(qWeatherStore) as T
//            }
//            modelClass.isAssignableFrom(LoggerViewModel::class.java) -> {
//                @Suppress("UNCHECKED_CAST")
//                return LoggerViewModel(repositoryFactory.create(LoggerRepository::class.java)) as T
//            }
//            modelClass.isAssignableFrom(PortainerViewModel::class.java)->{
//                @Suppress("UNCHECKED_CAST")
//                return PortainerViewModel(portainerStore) as T
//            }
//            modelClass.isAssignableFrom(AppInfoViewModel::class.java)->{
//                @Suppress("UNCHECKED_CAST")
//                return AppInfoViewModel(settingStore,pm) as T
//            }
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
}