package com.pushdeer.os.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationUtil {

    const val HIGH_CHANNEL_ID = "high_system"

    fun setupChannel(context: Context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    HIGH_CHANNEL_ID,
                    "服务提醒",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
    }
}