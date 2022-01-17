package com.wh.common.type

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class Json(private val v: String) {

    fun <T> toObject(modelClass: Class<T>): T {
        return JSONObject.parseObject(v, modelClass)
    }

    fun <T> toList(modelClass: Class<T>): List<T> {
        return JSONArray.parseArray(v, modelClass).toList()
    }

    fun toRequestBody(): RequestBody {
        return v.toRequestBody("application/json".toMediaType())
    }
}