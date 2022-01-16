package com.wh.common.typeExt

fun Float.toDot2NumStr(): String {
    return String.format("%.2f",this)
}

fun Float.toDot2NumMoneyStr(): String {
    return String.format("￥%.2f",this)
}