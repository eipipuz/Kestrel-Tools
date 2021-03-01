@file:Suppress("NOTHING_TO_INLINE")

package com.eipipuz


internal object Swapper {
    inline fun <T> swap(items: MutableList<T>, index: Int, otherIndex: Int) {
        val temp = items[index]
        items[index] = items[otherIndex]
        items[otherIndex] = temp
    }

    inline fun <T> findSmallAndBig(value: T, otherValue: T, isSmaller: (T, T) -> Boolean): Pair<T, T> = if (isSmaller(value, otherValue)) {
        Pair(value, otherValue)
    } else {
        Pair(otherValue, value)
    }
}