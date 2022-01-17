package com.pushdeer.os.keeper

import com.pushdeer.os.data.database.AppDatabase
import com.pushdeer.os.data.repository.LogDogRepository
import com.pushdeer.os.data.repository.MessageRepository
import com.pushdeer.os.data.repository.MiPushRepository

class RepositoryKeeper(database: AppDatabase) {
    val miPushRepository = MiPushRepository()
    val logDogRepository = LogDogRepository(database.logDogDao())
    val messageRepository = MessageRepository(database.messageDao())
}