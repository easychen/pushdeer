package com.pushdeer.common.api

import com.pushdeer.common.api.data.response.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PushDeerApi {
    companion object {
        private const val baseUrl = "https://api2.pushdeer.com"

        fun create(): PushDeerApi {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PushDeerApi::class.java)
        }
    }

    @FormUrlEncoded
    @POST("/login/idtoken")
    suspend fun loginIdToken(@Field("idToken") idToken: String): ReturnData<TokenOnly>

//    @GET("/login/fake")
//    suspend fun fakeLogin(): ReturnData<TokenOnly>

    @FormUrlEncoded
    @POST("/user/info")
    suspend fun userInfo(@Field("token") token: String): ReturnData<UserInfo>

    @FormUrlEncoded
    @POST("/device/reg")
    suspend fun deviceReg(@FieldMap data: Map<String, String>): ReturnData<DeviceInfoList>

    @FormUrlEncoded
    @POST("/device/list")
    suspend fun deviceList(@Field("token") token: String): ReturnData<DeviceInfoList>

    @FormUrlEncoded
    @POST("/device/remove")
    suspend fun deviceRemove(@Field("token") token: String, @Field("id") id: Int): String

    @FormUrlEncoded
    @POST("/device/rename")
    suspend fun deviceRename(
        @Field("token") token: String,
        @Field("id") id: Int,
        @Field("name") newName: String
    ): String

    @FormUrlEncoded
    @POST("/key/gen")
    suspend fun keyGen(@Field("token") token: String): ReturnData<PushKeyList>

    @FormUrlEncoded
    @POST("/key/regen")
    suspend fun keyRegen(@FieldMap data: Map<String, String>): String

    @FormUrlEncoded
    @POST("/key/list")
    suspend fun keyList(@Field("token") token: String): ReturnData<PushKeyList>

    @FormUrlEncoded
    @POST("/key/remove")
    suspend fun keyRemove(@FieldMap data: Map<String, String>): String

    @FormUrlEncoded
    @POST("/key/rename")
    suspend fun keyRename(
        @Field("token") token: String,
        @Field("id") id: String,
        @Field("name") newName: String
    ): String

    // pushkey text desp type:text/image/markdown
    @FormUrlEncoded
    @POST("/message/push")
    suspend fun messagePush(@FieldMap data: Map<String, String>): String

    @FormUrlEncoded
    @POST("/message/list")
    suspend fun messageList(@Field("token") token: String): ReturnData<MessageList>

    @FormUrlEncoded
    @POST("/message/remove")
    suspend fun messageRemove(@Field("token") token: String, @Field("id") id: Int): String
}