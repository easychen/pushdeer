package com.wh.common.toy.qweather.store

import android.content.Context
import com.wh.common.store.IStore
import com.wh.common.store.Store
import com.wh.common.store.asStoreProvider

class QWeatherStore(context: Context): IStore {
    private val store = Store(
        context.getSharedPreferences(
            "q-weather",
            Context.MODE_PRIVATE
        ).asStoreProvider()
    )

    companion object{
        const val KEY_SERVICE_KEY = "service-key"
    }

    var serviceKey by store.string(KEY_SERVICE_KEY,"")
}