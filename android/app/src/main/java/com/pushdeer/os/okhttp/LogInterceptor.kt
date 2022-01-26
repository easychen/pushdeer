package com.pushdeer.os.okhttp

import android.util.Log
import com.pushdeer.os.data.repository.LogDogRepository
import okhttp3.Interceptor
import okhttp3.Response

class LogInterceptor(private val logDogRepository: LogDogRepository): Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val url = request.url
        val methods = request.method
        val isHttps = request.isHttps
        val contentType = request.body?.contentType()

        val response = chain.proceed(request)

        Log.d("WH_", "intercept: $response.")

        return response
    }
}