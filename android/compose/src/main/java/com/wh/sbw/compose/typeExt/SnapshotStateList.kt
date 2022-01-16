package com.wh.sbw.compose.typeExt

import androidx.compose.runtime.snapshots.SnapshotStateList

fun <T>SnapshotStateList<T>.addOrSkip(t:T){
    if (this.contains(t)){
        return
    }else{
        this.add(t)
    }
}