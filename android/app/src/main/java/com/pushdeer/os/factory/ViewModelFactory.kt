package com.pushdeer.os.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pushdeer.os.data.api.PushDeerApi
import com.pushdeer.os.keeper.RepositoryKeeper
import com.pushdeer.os.keeper.StoreKeeper
import com.pushdeer.os.viewmodel.LogDogViewModel
import com.pushdeer.os.viewmodel.MessageViewModel
import com.pushdeer.os.viewmodel.PushDeerViewModel
import com.pushdeer.os.viewmodel.UiViewModel

class ViewModelFactory(
    private val repositoryKeeper: RepositoryKeeper,
    private val storeKeeper: StoreKeeper,
    private val pushDeerService: PushDeerApi
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(UiViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return UiViewModel(settingStore = storeKeeper.settingStore) as T
            }
            modelClass.isAssignableFrom(PushDeerViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return PushDeerViewModel(
                    settingStore = storeKeeper.settingStore,
                    logDogRepository = repositoryKeeper.logDogRepository,
                    pushDeerService = pushDeerService,
                    messageRepository = repositoryKeeper.messageRepository
                ) as T
            }
            modelClass.isAssignableFrom(LogDogViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return LogDogViewModel(repositoryKeeper.logDogRepository) as T
            }
            modelClass.isAssignableFrom(MessageViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return MessageViewModel(
                    messageRepository = repositoryKeeper.messageRepository,
                    pushDeerService = pushDeerService,
                    settingStore = storeKeeper.settingStore
                ) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}