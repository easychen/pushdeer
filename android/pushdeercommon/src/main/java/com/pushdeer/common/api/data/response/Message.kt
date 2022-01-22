package com.pushdeer.common.api.data.response


class Message {
    var id = 0
    var uid: String? = null
    var text: String? = null
    var desp: String? = null
    var type: String? = null
    var pushkey_name: String? = null
    var created_at: String? = null
}

class MessageList {
    var messages = emptyList<Message>()
}