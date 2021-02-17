package com.eipipuz


class Queue<T>(vararg initialValues: T) {
    companion object {
        fun <T> from(list: List<T>): Queue<T> {
            return Queue<T>().apply {
                list.forEach {
                    enqueue(it)
                }
            }
        }
    }

    private val values = initialValues.toMutableList()

    fun enqueue(value: T) {
        values.add(value)
    }

    fun dequeue(): T? {
        if (values.isEmpty()) return null

        val first = values.first()
        values.removeAt(0)

        return first
    }

    fun isEmpty() = values.isEmpty()

    fun toList(): List<T> = values.toList()
}