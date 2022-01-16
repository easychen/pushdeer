package com.wh.common.toy.qweather.helper

import com.wh.common.toy.qweather.data.QWeather
import com.wh.common.typeExt.toJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

object QWeatherHttpApiHelper {
    // https://api.qweather.com/v7/weather/now?


    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    private fun getRequest(): Request {
        val baseUrl = "https://devapi.qweather.com/v7/weather/now".toHttpUrl().newBuilder().apply {
            addQueryParameter("key", "1dcaaf15508b4170adbbe5302b860200")
            addQueryParameter("location", "39.91337035811204,116.40148390744017")
            addQueryParameter("unit", "m")
        }.build()
        return Request.Builder().url(baseUrl).get().build()
    }

    suspend fun fetchRealTimeWeather(): QWeather {
        return withContext(Dispatchers.IO) {
            val response = getClient().newCall(getRequest()).execute()
            if (!response.isSuccessful || response.code!=200) {
                throw IOException("error in fetchRealTimeWeather error code ${response.code} ${response.message}")
            }
            val body = response.body ?: throw IOException()
            val s = body.string()
            body.close()
            return@withContext s.toJson().toObject(QWeather::class.java)
        }
    }
}