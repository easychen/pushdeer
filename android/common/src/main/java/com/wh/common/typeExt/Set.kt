package com.wh.common.typeExt

fun <T>Set<T>.addOrRemove(t:T): Set<T> {
    val tmp = this.toMutableSet()
    if (tmp.contains(t)){
        tmp.remove(t)
    }else{
        tmp.add(t)
    }
    return tmp.toSet()
}