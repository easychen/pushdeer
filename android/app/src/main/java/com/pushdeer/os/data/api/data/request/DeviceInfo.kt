package com.pushdeer.os.data.api.data.request

class DeviceInfo {
    var id:Int = 0
    var uid:String = ""
    var name:String = ""
    var type:String = ""
    var device_id: String = ""
    var is_clip: Int = 0

    fun toRequestMap(token:String): Map<String, String> {
        return mapOf(
            "token" to token,
            "name" to name,
            "device_id" to device_id,
            "is_clip" to is_clip.toString(),
            "type" to "android"
        )
    }

    override fun toString(): String {
        return "id:$id uid:$uid name:$name type:$type device_id:$device_id is_clip:$is_clip"
    }
}