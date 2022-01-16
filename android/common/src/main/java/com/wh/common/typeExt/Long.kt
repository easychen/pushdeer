package com.wh.common.typeExt

import com.wh.common.util.TimeUtils
import kotlin.math.absoluteValue

fun Long.toTimestamp(spf: String = "yyyy-MM-dd HH:mm:ss"): String {
    return TimeUtils.getFormattedTime(this, spf)
}

fun Long.timestampMSToDayLevel(): Long {
    return this / 86_400_000
}

fun Long.daysBetween(otherTime: Long): Int {
    return (this.timestampMSToDayLevel() - otherTime.timestampMSToDayLevel()).absoluteValue.toInt() + 1
}

fun Long.daysBetween(): Int {
    return (this.absoluteValue / 86_400_000).toInt() + 1
}