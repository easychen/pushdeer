package com.pushdeer.os.data.repository

import com.pushdeer.os.data.database.dao.LogDogDao
import com.pushdeer.os.data.database.entity.LogDog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LogDogRepository(private val logDogDao: LogDogDao) {
    val all = logDogDao.all

    suspend fun clear(){
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