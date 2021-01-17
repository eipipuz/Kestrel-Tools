package com.eipipuz

import kotlin.math.max

class MultiSet<T>() : MutableSet<T> {
    val items: MutableMap<T, Int> = mutableMapOf()

    constructor(elements: Collection<T>) : this() {
        elements.forEach {
            val previousValue = items[it] ?: 0
            items[it] = previousValue + 1
        }
    }

    constructor(vararg elements: T) : this() {
        addAll(elements.toList())
    }

    override fun addAll(elements: Collection<T>): Boolean {
        var result = false
        elements.forEach {
            result = add(it) || result
        }

        return result
    }

    override fun clear() {
        items.clear()
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        var result = false
        elements.forEach {
            result = remove(it) || result
        }

        return result
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val multiSet = MultiSet(elements)
        val result = this == multiSet
        val intersectionMultiSet = intersection(multiSet)
        items.clear()
        items.putAll(intersectionMultiSet.items)

        return result
    }

    override val size: Int
        get() = items.size

    override fun containsAll(elements: Collection<T>) = elements.all { it in this }

    override fun add(element: T): Boolean {
        val previousValue = items[element] ?: 0
        items[element] = previousValue + 1

        return true
    }

    override operator fun contains(element: T) = items.containsKey(element)

    fun clone() = MultiSet(this)

    fun union(multiSet: MultiSet<T>): MultiSet<T> {
        val result = clone()
        multiSet.items.forEach { (k, v) ->
            val previousValue = result.items[k] ?: 0
            result.items[k] = max(previousValue, v)
        }

        return result
    }

    fun intersection(multiSet: MultiSet<T>): MultiSet<T> {
        val commonKeys = items.keys.intersect(multiSet.items.keys)
        val result = MultiSet<T>()
        commonKeys.forEach {
            val leftValue = items.getValue(it)
            val rightValue = multiSet.items.getValue(it)
            result.items[it] = Math.min(leftValue, rightValue)
        }

        return result
    }

    override fun isEmpty() = items.isEmpty()

    override fun remove(element: T): Boolean = when (val count = items[element]) {
        null -> false
        1 -> {
            items.remove(element)

            true
        }
        else -> {
            items[element] = count - 1

            true
        }
    }

    override fun iterator(): MutableIterator<T> = object : MutableIterator<T> {
        val items: MutableMap<T, Int> = this@MultiSet.items.toMutableMap()

        override fun hasNext(): Boolean = items.isNotEmpty()

        override fun next(): T {
            val result = items.keys.first()
            when (val count = items[result]) {
                null -> throw IllegalStateException("Can't next")
                1 -> items.remove(result)
                else -> {
                    items[result] = count - 1
                }
            }

            return result
        }

        override fun remove() {
            val key = items.keys.last()
            val count = items[key] ?: throw IllegalStateException("can't remove key:$key")
            if (count > 1) {
                items[key] = count -1
            } else {
                items.remove(key)
            }
        }
    }

    override fun toString() = "MultiSet(items=$items)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MultiSet<*>

        return items == other.items
    }

    override fun hashCode() = items.hashCode()
}