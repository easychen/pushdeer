package com.pushdeer.os.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pushdeer.os.data.database.dao.LogDogDao
import com.pushdeer.os.data.database.dao.MessageDao
import com.pushdeer.os.data.database.entity.LogDog
import com.pushdeer.os.data.database.entity.MessageEntity

@Database(entities = [LogDog::class,MessageEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun logDogDao(): LogDogDao
    abstract fun messageDao():MessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "app-database"
                    ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}