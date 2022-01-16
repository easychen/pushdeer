package com.pushdeer.os.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.pushdeer.os.data.database.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @get:Query("select * from message order by id desc")
    val all: Flow<List<MessageEntity>>

    @Delete
    suspend fun delete(vararg messageEntity: MessageEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg messageEntity: MessageEntity)
}