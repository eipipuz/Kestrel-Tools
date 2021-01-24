package com.eipipuz

import com.eipipuz.Swapper.swap


class Heap<T : Comparable<T>> private constructor(internal val items: MutableList<T>) {
    companion object {
        fun <T : Comparable<T>> from(vararg elements: T): Heap<T> = from(elements.toList())

        @Suppress("MemberVisibilityCanBePrivate")
        fun <T : Comparable<T>> from(collection: Collection<T>): Heap<T> {
            val items = collection.toMutableList()

            var index: Int = (items.size - 1) / 2
            do {
                siftDown(items, index, items.size)
                index--
            } while (index >= 0)

            return Heap(items)
        }

        fun <T : Comparable<T>> createEmpty(): Heap<T> = Heap(mutableListOf())

        internal tailrec fun <T : Comparable<T>> siftDown(items: MutableList<T>, index: Int, size: Int) {
            val leftIndex = 2 * index + 1
            val rightIndex = 2 * index + 2

            var maxIndex = if (leftIndex < size && items[leftIndex] > items[index]) {
                leftIndex
            } else {
                index
            }
            if (rightIndex < size && items[rightIndex] > items[maxIndex]) {
                maxIndex = rightIndex
            }

            if (maxIndex != index) {
                swap(items, index, maxIndex)
                siftDown(items, maxIndex, size)
            }
        }

        internal fun <T : Comparable<T>> siftUp(items: MutableList<T>, index: Int) {
            val newElement = items[index] // This saves swaps
            var currentIndex = index

            while (currentIndex > 0) {
                val parentIndex = (currentIndex - 1) / 2
                val parentElement = items[parentIndex]
                if (parentElement < newElement) {
                    items[currentIndex] = parentElement
                    currentIndex = parentIndex
                } else {
                    break
                }
            }
            items[currentIndex] = newElement
        }
    }

    val max: T?
        get() = if (isEmpty) null else items.first()

    fun push(element: T) {
        items.add(element)
        siftUp(items, items.lastIndex)
    }

    fun pop(): T? {
        val result = max ?: return null

        val smallElement = items.removeAt(items.lastIndex)
        if (isEmpty) return result

        items[0] = smallElement
        siftDown(items, 0, items.size)

        return result
    }

    val size: Int
        get() = items.size

    val isEmpty: Boolean
        get() = size == 0
}