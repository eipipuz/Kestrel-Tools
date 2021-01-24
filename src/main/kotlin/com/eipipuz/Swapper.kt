package com.eipipuz


object Swapper {
    internal fun <T> swap(items: MutableList<T>, index: Int, otherIndex: Int) {
        val temp = items[index]
        items[index] = items[otherIndex]
        items[otherIndex] = temp
    }
}