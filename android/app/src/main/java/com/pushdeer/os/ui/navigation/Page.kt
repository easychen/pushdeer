package com.pushdeer.os.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.pushdeer.os.R

sealed class Page(val route: String, @StringRes val labelStringId:Int, @DrawableRes val id : Int) {
    object Devices : Page("device",R.string.main_device, R.drawable.ipad_and_iphon2x)
    object Keys:Page("key",R.string.main_key,R.drawable.key2x)
    object Messages:Page("message",R.string.main_message,R.drawable.message2x)
    object Settings:Page("setting",R.string.main_setting,R.drawable.gearshape2x)
}

val pageList = listOf(
    Page.Devices,Page.Keys,Page.Messages,Page.Settings
)