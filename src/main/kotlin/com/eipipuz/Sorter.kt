package com.eipipuz

import com.eipipuz.Swapper.swap
import com.eipipuz.graph.Graph


object Sorter {
    private fun <T : Comparable<T>> findPivot(list: List<T>): T {
        val start = list.first()
        val end = list.last()
        val middleIndex: Int = list.size / 2
        val middle = list[middleIndex]

        return if (start <= end) {
            if (start <= middle) {
                if (middle <= end) middle else end
            } else {
                start
            }
        } else {
            if (end <= middle) {
                if (middle <= start) middle else start
            } else {
                end
            }
        }
    }

    fun <T : Comparable<T>> quickSort(collection: Collection<T>): List<T> {
        val input = collection.toList()
        if (collection.size <= 1) return input

        val pivot = findPivot(input)

        val lessThanList = mutableListOf<T>()
        val equalThanList = mutableListOf<T>()
        val moreThanList = mutableListOf<T>()

        input.forEach {
            when (it.compareTo(pivot)) {
                -1 -> lessThanList.add(it)
                0 -> equalThanList.add(it)
                1 -> moreThanList.add(it)
            }
        }

        val lessThanSorted = quickSort(lessThanList)
        val moreThanSorted = quickSort(moreThanList)

        return lessThanSorted + equalThanList + moreThanSorted
    }

    fun <T : Comparable<T>> mergeSort(collection: Collection<T>): List<T> {
        val input = collection.toList()
        if (input.size <= 1) return input

        val middleIndex: Int = input.size / 2
        val firstHalf = input.subList(0, middleIndex)
        val firstSortedHalf = mergeSort(firstHalf)
        val secondHalf = input.subList(middleIndex, input.size)
        val secondSortedHalf = mergeSort(secondHalf)

        var firstIndex = 0
        var secondIndex = 0
        return List(input.size) {
            when {
                firstIndex >= firstSortedHalf.size -> {
                    val secondHalfElement = secondSortedHalf[secondIndex]
                    secondIndex++

                    secondHalfElement
                }
                secondIndex >= secondSortedHalf.size -> {
                    val firstHalfElement = firstSortedHalf[firstIndex]
                    firstIndex++

                    firstHalfElement
                }
                else -> {
                    val firstHalfElement = firstSortedHalf[firstIndex]
                    val secondHalfElement = secondSortedHalf[secondIndex]

                    if (firstHalfElement < secondHalfElement) {
                        firstIndex++

                        firstHalfElement
                    } else {
                        secondIndex++

                        secondHalfElement
                    }
                }
            }
        }
    }

    fun <T : Comparable<T>> heapSort(collection: Collection<T>): List<T> {
        val inputs = collection.toMutableList()
        if (collection.size <= 1) return inputs

        val result = Heap.from(inputs).items
        var currentIndex = result.lastIndex
        do {
            swap(result, 0, currentIndex)
            Heap.siftDown(result, 0, currentIndex)
            currentIndex--
        } while (currentIndex > 0)

        return result
    }

    fun <T : Comparable<T>> insertSort(collection: Collection<T>): List<T> {
        val mutableList = collection.toMutableList()
        if (collection.size <= 1) return mutableList

        var previousValue = mutableList.first()
        repeat(mutableList.size) { index ->
            val nextValue = mutableList[index]
            if (nextValue < previousValue) {
                val (shouldBeIndex, _) = Searcher.binarySearchIndex(mutableList.subList(0, index), nextValue)

                mutableList.removeAt(index)
                mutableList.add(shouldBeIndex, nextValue)
            }
            previousValue = nextValue
        }

        return mutableList
    }

    fun <T : Comparable<T>> topologicalSort(graph: Graph<T>): List<T> {
        val valueToIncomingCount = graph.valueToIncomingCount.toMutableMap()
        val initialValues = valueToIncomingCount.filter { it.value == 0 }.keys
        var valuesToProcess = graph.valueToEdges.keys.size

        val heap = Heap.from(initialValues)
        val sortedList = mutableListOf<T>()

        while (!heap.isEmpty) {
            val value = heap.pop()!!
            sortedList.add(value)
            valuesToProcess--

            val edges = graph.valueToEdges.getOrDefault(value, emptySet())
            for ((otherValue, _) in edges) {
                val count = valueToIncomingCount[otherValue]!!
                if (count == 1) {
                    heap.push(otherValue)
                } else {
                    valueToIncomingCount[otherValue] = count - 1
                }
            }
        }

        if (valuesToProcess > 0) {
            throw UnsupportedOperationException("Topological sorting only works for DAGs.")
        }

        return sortedList
    }
}