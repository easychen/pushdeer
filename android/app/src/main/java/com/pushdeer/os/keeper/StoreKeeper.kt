package com.pushdeer.os.keeper

import android.content.Context
import com.pushdeer.os.store.SettingStore

class StoreKeeper(context: Context) {
    val settingStore = SettingStore(context)
}