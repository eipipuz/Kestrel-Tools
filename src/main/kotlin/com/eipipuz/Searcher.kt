package com.eipipuz

import com.eipipuz.Swapper.swap


object Searcher {
    internal fun <T : Comparable<T>> binarySearchIndex(list: List<T>, searchedElement: T): Pair<Int, Boolean> {
        if (list.isEmpty()) return Pair(0, false)
        if (list.first().compareTo(searchedElement) == 1) return Pair(0, false)
        if (list.last().compareTo(searchedElement) == -1) return Pair(list.size, false)

        var lowerIndex = 0
        var upperIndex = list.lastIndex

        while (true) {
            val middleIndex: Int = lowerIndex + (upperIndex - lowerIndex) / 2

            when (list[middleIndex].compareTo(searchedElement)) {
                -1 -> {
                    if (lowerIndex == middleIndex) {
                        // This happens when lower and upper are apart by 1.
                        when (list[upperIndex].compareTo(searchedElement)) {
                            -1 -> return Pair(lowerIndex, false)
                            0 -> return Pair(upperIndex, true)
                            1 -> return Pair(upperIndex, false)
                        }
                    } else {
                        lowerIndex = middleIndex
                    }
                }
                0 -> return Pair(middleIndex, true)
                1 -> {
                    upperIndex = middleIndex
                }
            }
        }
    }

    fun <T : Comparable<T>> quickSelect(list: MutableList<T>, nth: Int): T? {
        if (nth < 0 || nth >= list.size) return null

        return quickSelect(list, nth, 0, list.lastIndex)
    }

    private tailrec fun <T : Comparable<T>> quickSelect(
        list: MutableList<T>,
        nth: Int,
        startIndex: Int,
        endIndex: Int
    ): T {
        if (nth !in startIndex..endIndex) {
            throw IllegalArgumentException("nth: $nth should be [$startIndex, $endIndex]")
        }
        if (startIndex == endIndex) return list[startIndex]

        val pivotIndex = findPivotIndex(list, startIndex, endIndex)
        val pivot = list[pivotIndex]

        swap(list, pivotIndex, endIndex)

        var leftIndex = startIndex
        var rightIndex = endIndex - 1
        var lastSwapIndex = leftIndex - 1 // This will be the value if all values are bigger than pivot

        while (leftIndex < rightIndex) {
            val left = list[leftIndex]
            if (left < pivot) {
                leftIndex++
            } else {
                swap(list, leftIndex, rightIndex)
                lastSwapIndex = leftIndex
                rightIndex--
            }
        }

        val newPivotIndex = lastSwapIndex + 1
        swap(list, newPivotIndex, endIndex)

        return when {
            nth == newPivotIndex -> list[nth]
            nth < newPivotIndex -> quickSelect(list, nth, startIndex, newPivotIndex - 1)
            nth > newPivotIndex -> quickSelect(list, nth, newPivotIndex + 1, endIndex)
            else -> throw IllegalStateException("Impossible scenario between nth:$nth and newPivotIndex:$newPivotIndex")
        }
    }

    private fun <T : Comparable<T>> findPivotIndex(list: List<T>, startIndex: Int, endIndex: Int): Int {
        val start = list[startIndex]
        val end = list[endIndex]
        val middleIndex: Int = startIndex + (endIndex - startIndex) / 2
        val middle = list[middleIndex]

        return if (start <= end) {
            if (start <= middle) {
                if (middle <= end) middleIndex else endIndex
            } else {
                startIndex
            }
        } else {
            if (end <= middle) {
                if (middle <= start) middleIndex else startIndex
            } else {
                endIndex
            }
        }
    }

    fun <T> breathFirstSearch(graph: Graph<T>, initialValue: T, graphObserver: GraphObserver<T>) {
        val initialVertexId = graph.valueToVertexId[initialValue] ?: throw IllegalArgumentException("Unknown value($initialValue)")

        val vertexIdQueue = Queue(initialVertexId)
        val addedVertexIds = mutableSetOf(initialVertexId)

        while (true) {
            val currentVertexId = vertexIdQueue.getFirst() ?: return

            val currentValue = graph.vertexIdToValue[currentVertexId]!!

            graphObserver.onVertexFound(currentValue)
            val edges = graph.vertexIdToEdges[currentVertexId] ?: emptyList()
            for ((vertexId, weight) in edges) {
                if(vertexId in addedVertexIds) continue

                vertexIdQueue.add(vertexId)
                addedVertexIds.add(vertexId)
                val toValue = graph.vertexIdToValue[currentVertexId]!!
                graphObserver.onEdge(currentValue, toValue, weight)
            }
            graphObserver.afterVertexFound(currentValue)
        }
    }

    fun <T> depthFirstSearch(graph: Graph<T>, initialValue: T, graphObserver: GraphObserver<T>) {
        val initialVertexId = graph.valueToVertexId[initialValue] ?: throw IllegalArgumentException("Unknown value($initialValue)")

        val vertexIdStack = Stack(initialVertexId)
        val addedVertexIds = mutableSetOf(initialVertexId)

        while (!vertexIdStack.isEmpty()) {
            val currentVertexId = vertexIdStack.getLast() ?: return

            val currentValue = graph.vertexIdToValue[currentVertexId]!!

            graphObserver.onVertexFound(currentValue)
            val edges = graph.vertexIdToEdges[currentVertexId]?.reversed() ?: emptyList()
            for ((vertexId, weight) in edges) {
                if(vertexId in addedVertexIds) continue

                vertexIdStack.add(vertexId)
                addedVertexIds.add(vertexId)

                val toValue = graph.vertexIdToValue[currentVertexId]!!
                graphObserver.onEdge(currentValue, toValue, weight)
            }
            graphObserver.afterVertexFound(currentValue)
        }
    }
}