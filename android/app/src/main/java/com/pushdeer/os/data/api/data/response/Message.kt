package com.pushdeer.os.data.api.data.response

import com.pushdeer.os.data.database.entity.MessageEntity

class Message : MessageEntity() {
    fun toMessageEntity(): MessageEntity {
        return MessageEntity().apply {
            this.id = this@Message.id
            this.uid = this@Message.uid
            this.text = this@Message.text
            this.desp = this@Message.desp
            this.type = this@Message.type
            this.pushkey_name = this@Message.pushkey_name
            this.created_at = this@Message.created_at
        }
    }
}

class MessageList {
    var messages = emptyList<Message>()
}