package com.pushdeer.os.data.repository

import com.pushdeer.os.data.database.dao.LogDogDao
import com.pushdeer.os.data.database.entity.LogDog
import com.pushdeer.os.store.SettingStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LogDogRepository(private val logDogDao: LogDogDao,private val settingStore: SettingStore) {
    val all = logDogDao.all

    suspend fun clear() {
        logDogDao.clear()
    }

    suspend fun insert(vararg logDog: LogDog) {
        logDogDao.insert(*logDog)
    }

    suspend fun logd(entity: String, event: String, log: String) {
        withContext(Dispatchers.IO) {
            insert(LogDog.logd(entity, event, log))
        }
    }

    suspend fun loge(entity: String, event: String, log: String) {
        withContext(Dispatchers.IO) {
            insert(LogDog.loge(entity, event, log))
        }
    }

    fun log(
        entity: String,
        level: String,
        event: String,
        log: String
    ) {
        logDogDao.insert1(LogDog().apply {
            this.entity = entity
            this.level = level
            this.event = event
            this.log = log
        })
    }

    suspend fun logi(entity: String, event: String, log: String) {
        withContext(Dispatchers.IO) {
            insert(LogDog.logi(entity, event, log))
        }
    }

    suspend fun logw(entity: String, event: String, log: String) {
        withContext(Dispatchers.IO) {
            insert(LogDog.logw(entity, event, log))
        }
    }
}