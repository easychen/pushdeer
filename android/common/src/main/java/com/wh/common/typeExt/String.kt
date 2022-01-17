package com.wh.common.typeExt

import com.wh.common.type.Json
import com.wh.common.util.Base64Utils
import com.wh.common.util.TimeUtils

fun String.timestampParse(spf:String = "yyyy-MM-dd HH:mm:ss"): Long {
    return TimeUtils.getMSFromFormattedTime(spf)
}

fun String.base64Decode(): String {
    return Base64Utils.decode(this)
}

fun String.base64Encode(): String {
    return Base64Utils.encode(this).replace("\n","")
}

fun String.toJson(): Json {
    return Json(this)
}