package com.wh.sbw.compose.typeExt

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun <T> LiveData<T>.asMutableState(lifecycleOwner: LifecycleOwner, default: T): MutableState<T> {
    val tmp = mutableStateOf(default)
    this.observe(lifecycleOwner) {
        tmp.value = it
    }
    return tmp
}

fun <T> LiveData<T>.asMutableState(lifecycleOwner: LifecycleOwner): MutableState<T?> {
    val tmp = mutableStateOf(this.value)
    this.observe(lifecycleOwner) {
        tmp.value = it
    }
    return tmp
}