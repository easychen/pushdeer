package com.wh.common.typeExt

fun Int.daysToInterval(): Long {
    return (this-1) * 86_400_000L
}