package com.pushdeer.common.api.data.response

class UserInfo {
    var id: String = ""
    var name: String = ""
    var email: String = ""
    var app_id: String = ""
    var wechat_id: String = ""
    var level: Int = 1
    var created_at: String = ""
    var updated_at: String = ""

    override fun toString(): String {
        return "id:$id\n" +
                "name:$name\n" +
                "email:$email\n" +
                "app_id:$app_id\n" +
                "wechat_id:$wechat_id\n" +
                "level:$level\n" +
                "created:$created_at\n" +
                "updated:$updated_at"
    }
}