package com.pushdeer.common.api.data.response

class PushKey {
    var id:String = ""
    var key: String = ""
    var name: String = ""
    var created_at = ""

    override fun toString(): String {
        return "id:$id key:$key name:$name created_at:$created_at"
    }
}

class PushKeyList {
    var keys: List<PushKey> = emptyList()

    override fun toString(): String {
        return "keys:$keys"
    }
}