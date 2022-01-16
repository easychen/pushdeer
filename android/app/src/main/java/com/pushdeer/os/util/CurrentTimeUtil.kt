package com.pushdeer.os.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

object CurrentTimeUtil {
    @SuppressLint("SimpleDateFormat")
    val ymdFmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @SuppressLint("SimpleDateFormat")
    val ymdthmssFmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")

    private val currentTimeZone: TimeZone = TimeZone.getDefault()

    private val tz2utcMSOffset = currentTimeZone.getOffset(System.currentTimeMillis())

    fun utcTS2ms(utcTS: String): Long {
        val calendar = Calendar.getInstance(currentTimeZone)
        val date = ymdthmssFmt.parse(utcTS)!!
        calendar.time = date
        return calendar.time.time + tz2utcMSOffset
    }

    fun msTSDis(now: Long, then: Long): String {
        val dis = abs(now - then)
        return when {
            dis < 60_000 -> {
                (dis / 1_000).toString() + "s ago"
            }
            dis < 3_600_000 -> {
                (dis / 60_000).toString() + "min ago"
            }
            dis < 86_400_000 -> {
                (dis / 3_600_000).toString() + "h ago"
            }
            else -> {
                ymdFmt.format(Date(then))
            }
        }
    }

    fun resolveUTCTimeAndNow(utcTS: String, now: Long): String {
        return msTSDis(now, utcTS2ms(utcTS))
    }
}