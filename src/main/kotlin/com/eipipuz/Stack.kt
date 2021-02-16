package com.eipipuz


class Stack<T>(vararg initialValues: T) {
    private val values = initialValues.toMutableList()

    fun push(value: T) {
        values.add(value)
    }

    fun pop(): T? {
        if (values.isEmpty()) return null

        val last = values.last()
        values.removeAt(values.lastIndex)

        return last
    }

    fun isEmpty() = values.isEmpty()

    fun toList(): List<T> = values.reversed()
}