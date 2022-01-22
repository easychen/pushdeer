package com.pushdeer.common.api.data.response

class ReturnData<T> {
    var code: Int = 0
    var content: T?=null
    var error:String = ""

    override fun toString(): String {
        return "code:${code} error:${error} content:${content.toString()}"
    }
}

class TokenOnly{
    var token:String = ""
}