package com.wh.common.typeExt

import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.Drawable
import android.os.Build

fun Drawable.foreground():Drawable{
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
        this is AdaptiveIconDrawable && this.background == null
    ) {
        return this.foreground
    }
    return this
}