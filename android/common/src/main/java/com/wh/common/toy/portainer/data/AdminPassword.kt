package com.wh.common.toy.portainer.data

import com.alibaba.fastjson.JSONObject
import com.wh.common.type.Json


class AdminPassword {
    var username: String? = null
    var password: String? = null

    fun toJson(): Json {
        return Json(JSONObject.toJSONString(this))
    }

}