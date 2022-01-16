package com.wh.common.typeExt

fun <T> MutableList<T>.addOrRemove(t: T): MutableList<T> {
    if (this.contains(t)) {
        this.remove(t)
    } else {
        this.add(t)
    }
    return this
}
