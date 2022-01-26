package com.pushdeer.os.data.api

import com.pushdeer.os.data.api.data.response.*
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PushDeerApi {
    companion object {
        val baseUrl = "https://api2.pushdeer.com"
    }

    @FormUrlEncoded
    @POST("/login/idtoken")
    suspend fun loginWithAppleIdToken(@Field("idToken") idToken: String): ReturnData<TokenOnly>

    @FormUrlEncoded
    @POST("/login/wecode")
    suspend fun loginWithWeXin(@Field("code") code: String): ReturnData<TokenOnly>

    @FormUrlEncoded
    @POST("/user/merge")
    suspend fun userMerge(
        @Field("token") token: String,
        @Field("type") type: String, // apple wechat
        @Field("tokenorcode") tokenorcode: String // input idToken / code
    ): String

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