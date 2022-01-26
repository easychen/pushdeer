package com.pushdeer.os.data.api.data.response

class UserInfo {
    var id: String = ""
//    var uid: String = ""
    var name: String = ""
    var email: String = ""
    var apple_id: String? = ""
    var wechat_id: String? = ""
    var level: Int = 1
    var created_at: String = ""
    var updated_at: String = ""

    override fun toString(): String {
        return "id:$id\n" +
//                "uid:$uid\n" +
                "name:$name\n" +
                "email:$email\n" +
                "apple_id:$apple_id\n" +
                "wechat_id:$wechat_id\n" +
                "level:$level\n" +
                "created:$created_at\n" +
                "updated:$updated_at"
    }

    val isWeChatLogin: Boolean
        get() {
            return if (wechat_id == null) {
                false
            } else {
                wechat_id!!.length > 4
            }
        }
    val isAppleLogin: Boolean
        get() {
            return if (apple_id == null) {
                false
            } else {
                apple_id!!.length > 4
            }
        }

    val isLogin: Boolean
        get() = isWeChatLogin or isAppleLogin
}