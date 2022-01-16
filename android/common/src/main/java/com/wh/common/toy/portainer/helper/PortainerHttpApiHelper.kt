package com.wh.common.toy.portainer.helper

import com.wh.common.toy.portainer.data.AdminPassword
import com.wh.common.toy.portainer.data.Auth
import com.wh.common.toy.portainer.data.Endpoint
import com.wh.common.toy.portainer.data.Portainer
import com.wh.common.typeExt.toJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.SocketException

object PortainerHttpApiHelper {

    fun requestWithAuth(
        url: String,
        token: String
    ): Request {
        return Request.Builder()
            .url(url)
            .header(
                "Authorization",
                "Bearer $token"
            )
            .get()
            .build()
    }

    fun requestWithAdminPassword(url: String, username: String, password: String): Request {
        return Request.Builder()
            .post(
                AdminPassword().apply {
                    this.username = username
                    this.password = password
                }.toJson().toRequestBody()
            )
            .url(url)
            .build()
    }

    fun clientWithProxy(
        proxyType: Proxy.Type,
        host: String,
        port: Int
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .proxy(Proxy(proxyType, InetSocketAddress(host, port))).build()
    }

    suspend fun fetchPortainerAuth(
        baseUrl: String,
        username: String,
        password: String,
        proxyType: Proxy.Type,
        proxyHost: String,
        proxyPort: Int
    ): Auth {
        return withContext(Dispatchers.IO) {
            val url = "${baseUrl}/api/auth"
            val call = clientWithProxy(
                proxyType,
                proxyHost,
                proxyPort
            ).newCall(
                requestWithAdminPassword(
                    url,
                    username,
                    password
                )
            )
            val response = try {
                call.execute()
            } catch (e: SocketException) {
                throw e
            }
            val body = response.body ?: throw IOException()
            val s = body.string()
            body.close()
            return@withContext s.toJson().toObject(Auth::class.java).apply {
                setExpireTimestamp()
            }

        }
    }

    //http://192.168.51.99:9000/api/endpoints

    suspend fun fetchPortainerEndpoints(
        baseUrl: String,
        token: String,
        proxyType: Proxy.Type,
        proxyHost: String,
        proxyPort: Int
    ): List<Endpoint> {
        return withContext(Dispatchers.IO) {
            val url = "${baseUrl}/api/endpoints"
            val call = clientWithProxy(
                proxyType,
                proxyHost,
                proxyPort
            ).newCall(
                requestWithAuth(
                    url,
                    token
                )
            )
            val response = call.execute()
            val body = response.body ?: throw IOException()
            val s = body.string()
            body.close()
            return@withContext s.toJson().toList(Endpoint::class.java)
        }
    }


    suspend fun fetchPortainerContainer(
        baseUrl: String,
        endpointId: Int,
        token: String,
        proxyType: Proxy.Type,
        proxyHost: String,
        proxyPort: Int
    ): List<Portainer> {
        return withContext(Dispatchers.IO) {
            val url = "${baseUrl}/api/endpoints/${endpointId}/docker/containers/json"
            val call = clientWithProxy(
                proxyType,
                proxyHost,
                proxyPort
            ).newCall(
                requestWithAuth(
                    url,
                    token
                )
            )
            val response = call.execute()
            if (!response.isSuccessful) {
                throw IOException("error in fetchPortainerContainer ${response.code} ${response.message}")
            }
            val body = response.body ?: throw IOException()
            val s = body.string()
            body.close()
            return@withContext s.toJson().toList(Portainer::class.java)
        }
    }
}