package com.pushdeer.os.keeper

import com.pushdeer.os.data.database.AppDatabase
import com.pushdeer.os.data.repository.LogDogRepository
import com.pushdeer.os.data.repository.MessageRepository
import com.pushdeer.os.data.repository.MiPushRepository
import com.pushdeer.os.store.SettingStore

class RepositoryKeeper(database: AppDatabase,settingStore: SettingStore) {
    val miPushRepository = MiPushRepository()
    val logDogRepository = LogDogRepository(database.logDogDao(),settingStore)
    val messageRepository = MessageRepository(database.messageDao())
}