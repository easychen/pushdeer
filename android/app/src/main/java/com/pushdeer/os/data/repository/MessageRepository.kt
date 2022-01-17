package com.pushdeer.os.data.repository

import com.pushdeer.os.data.api.data.response.Message
import com.pushdeer.os.data.database.dao.MessageDao
import com.pushdeer.os.data.database.entity.MessageEntity

class MessageRepository(private val messageDao: MessageDao) {
    val all = messageDao.all

    suspend fun insert(vararg message: Message) {
        message.forEach {
            messageDao.insert(it.toMessageEntity())
        }
    }

    suspend fun delete(vararg message: Message) {
        message.forEach {
            messageDao.delete(it.toMessageEntity())
        }
    }

    suspend fun delete(vararg messageEntity: MessageEntity) {
            messageDao.delete(*messageEntity)
    }
}