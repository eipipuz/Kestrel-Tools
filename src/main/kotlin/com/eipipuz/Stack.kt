package com.eipipuz


class Stack<T>(vararg initialValues: T) {
    private val values = initialValues.toMutableList()

    fun add(value: T) {
        values.add(value)
    }

    fun getLast(): T? {
        if (values.isEmpty()) return null

        val last = values.last()
        values.removeAt(values.lastIndex)

        return last
    }

    fun isEmpty() = values.isEmpty()
}