package com.eipipuz

fun <T> MutableMap<T, Int>.setAsMultiSet(elements: Collection<T>) {
    elements.forEach {
        val previousValue = get(it) ?: 0
        set(it, previousValue + 1)
    }
}

class MultiSet<T>() : MutableSet<T> {
    private val items: MutableMap<T, Int> = mutableMapOf()

    private constructor(elements: Collection<T>) : this() {
        items.setAsMultiSet(elements)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        var result = false
        elements.forEach {
            result = result || add(it)
        }

        return result
    }

    override fun clear() {
        items.clear()
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        var result = false
        elements.forEach {
            result = result || remove(it)
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

    override operator fun contains(element: T) = items[element] != null

    fun clone() = MultiSet(this)

    fun union(multiSet: MultiSet<T>): MultiSet<T> {
        val result = clone()
        multiSet.items.forEach { k, v ->
            val previousValue = result.items[k] ?: 0
            result.items[k] = Math.max(previousValue, v)
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

    override fun remove(element: T): Boolean {
        val count = items[element]

        return when (count) {
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
    }

    override fun iterator(): MutableIterator<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toString() = "main.com.eipipuz.kestrel.MultiSet(items=$items)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MultiSet<*>

        return items != other.items
    }

    override fun hashCode() = items.hashCode()
}