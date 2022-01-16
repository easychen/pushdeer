package com.pushdeer.os.data.api.data.response

class DeviceRemove {
    var token: String = ""
    var id = ""

    fun toMap():Map<String,String> {
        return mapOf(
            "token" to token,
            "id" to id
        )
    }
}