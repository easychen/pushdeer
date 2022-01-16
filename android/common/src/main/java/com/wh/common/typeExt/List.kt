package com.wh.common.typeExt

inline fun <T> List<T>.everyN(n: Int, onEach: (List<T>) -> Unit) {
    val times = this.size / n
    var tmp = 0
    while (times > tmp) {
        onEach(this.getGroup(n, tmp))
        tmp += 1
    }
    if (size % n != 0) {
        onEach(getRest(times * n))
    }
}

// endIndex is not included
fun <T> List<T>.getRange(startIndex: Int, endIndex: Int): MutableList<T> {
    var startIndexAgent = startIndex
    val tmp = mutableListOf<T>()
    while (startIndexAgent < endIndex) {
        tmp.add(this[startIndexAgent])
        startIndexAgent += 1
    }
    return tmp
}

// have no range side check
// groupIndex start with 0
fun <T> List<T>.getGroup(groupSize: Int, groupIndex: Int): MutableList<T> {
    val startIndex = groupIndex * groupSize
    val endIndex = startIndex + groupSize
    return getRange(startIndex, endIndex)
}

// have no check
fun <T> List<T>.getRest(startIndex: Int): MutableList<T> {
    return getRange(startIndex, size)
}