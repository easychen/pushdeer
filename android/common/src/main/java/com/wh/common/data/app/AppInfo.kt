package com.wh.common.data.app

import android.graphics.drawable.Drawable

data class AppInfo(
    val packageName: String,
    val icon: Drawable,
    val label: String,
//    val installTime:Long,
//    val updateTime:Long
) {
    var selected: Boolean = false
}