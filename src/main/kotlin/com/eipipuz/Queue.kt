package com.eipipuz


class Queue<T>(vararg initialValues: T) {
    private val values = initialValues.toMutableList()

    fun add(value: T) {
        values.add(value)
    }

    fun getFirst(): T? {
        if (values.isEmpty()) return null

        val first = values.first()
        values.removeAt(0)

        return first
    }

    fun isEmpty() = values.isEmpty()
}