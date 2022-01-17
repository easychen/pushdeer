package com.wh.common.util

import android.util.Base64

class Base64Utils {
    companion object{
        fun decode(input: String): String {
            return String(Base64.decode(input,Base64.DEFAULT))
        }
        fun encode(input: String): String{
            return Base64.encodeToString(input.toByteArray(),Base64.DEFAULT)
        }
    }
}