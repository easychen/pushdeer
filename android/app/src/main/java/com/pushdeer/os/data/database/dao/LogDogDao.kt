package com.pushdeer.os.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.pushdeer.os.data.database.entity.LogDog
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDogDao {

    @get:Query("select * from LogDog Order by id desc")
    val all:Flow<List<LogDog>>

    @Insert
    suspend fun insert(vararg logDog: LogDog)

    @Insert
    fun insert1(vararg logDog: LogDog)

    @Query("delete from LogDog")
    suspend fun clear()
}