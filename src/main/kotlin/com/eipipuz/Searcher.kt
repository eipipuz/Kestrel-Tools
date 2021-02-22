package com.eipipuz

import com.eipipuz.Swapper.swap
import com.eipipuz.graph.*


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
        if (initialValue !in graph.valueToEdges.keys) {
            throw IllegalArgumentException("Unknown value($initialValue)")
        }

        val valueQueue = Queue(initialValue)
        val addedValues = mutableSetOf(initialValue)

        while (true) {
            val currentValue = valueQueue.dequeue() ?: return

            graphObserver.onVertexFound(currentValue)
            val edges = graph.valueToEdges[currentValue] ?: emptyList()
            for ((otherValue, weight) in edges) {
                if (otherValue in addedValues) continue

                valueQueue.enqueue(otherValue)
                addedValues.add(otherValue)
                graphObserver.onEdge(currentValue, otherValue, weight)
            }
            graphObserver.afterVertexFound(currentValue)
        }
    }

    fun <T> depthFirstSearch(graph: Graph<T>, initialValue: T, graphObserver: GraphObserver<T>) {
        if (initialValue !in graph.valueToEdges.keys) {
            throw IllegalArgumentException("Unknown value($initialValue)")
        }

        val vertexIdStack = Stack(initialValue)
        val addedValues = mutableSetOf(initialValue)

        while (!vertexIdStack.isEmpty()) {
            val currentValue = vertexIdStack.pop() ?: return

            graphObserver.onVertexFound(currentValue)
            val edges = graph.valueToEdges[currentValue]?.reversed() ?: emptyList()
            for ((otherValue, weight) in edges) {
                if (otherValue in addedValues) continue

                vertexIdStack.push(otherValue)
                addedValues.add(otherValue)

                graphObserver.onEdge(currentValue, otherValue, weight)
            }
            graphObserver.afterVertexFound(currentValue)
        }
    }

    fun <T> findLongestCommonSublist(list: List<T>, otherList: List<T>): List<T> {
        if (list.isEmpty()) return emptyList()
        if (otherList.isEmpty()) return emptyList()

        val matrix = Array(list.size) { Array(otherList.size) { 0 } }
        var maxLength = 0
        var endIndex = 0
        for (index in list.indices) {
            for (otherIndex in otherList.indices) {
                if (list[index] != otherList[otherIndex]) {
                    continue
                }

                val previousLength = if (index == 0 || otherIndex == 0) {
                    0
                } else {
                    matrix[index - 1][otherIndex - 1]
                }
                val newLength = 1 + previousLength
                matrix[index][otherIndex] = newLength

                if (newLength > maxLength) {
                    maxLength = newLength
                    endIndex = index
                }
            }
        }

        val startIndex = endIndex - maxLength + 1

        return list.subList(startIndex, startIndex + maxLength)
    }

    private fun <T> findMinEdge(valueToExplicitEdge: Map<T, ExplicitEdge<T>>): ExplicitEdge<T>? =
        valueToExplicitEdge.minByOrNull { (_, explicitEdge) ->
            explicitEdge.weight
        }?.value

    fun <T> findPrimsMinimumSpanningTree(graph: Graph<T>): Graph<T> {
        val mutableGraph = MutableGraph.createEmpty<T>()
        val valueToExplicitEdge = mutableMapOf<T, ExplicitEdge<T>>()
        val valuesToBeAdded = graph.valueToEdges.keys.toMutableSet()
        val valuesAlreadyInTree = mutableSetOf<T>()

        val firstValue = valuesToBeAdded.first()
        addFirstValue(graph, mutableGraph, valueToExplicitEdge, valuesToBeAdded, valuesAlreadyInTree, firstValue)

        while (valuesToBeAdded.isNotEmpty()) {
            val explicitEdge = findMinEdge(valueToExplicitEdge)!!
            addValueToTree(mutableGraph, valuesToBeAdded, valuesAlreadyInTree, explicitEdge)
            addCheaperEdges(graph, valueToExplicitEdge, valuesAlreadyInTree, explicitEdge)
            removeInvalidDestinationEdges(valueToExplicitEdge, explicitEdge.destinationValue)
        }

        return mutableGraph.toGraph()
    }

    private fun <T> addFirstValue(
        graph: Graph<T>,
        mutableGraph: MutableGraph<T>,
        valueToExplicitEdge: MutableMap<T, ExplicitEdge<T>>,
        valuesToBeAdded: MutableSet<T>,
        valuesAlreadyInTree: MutableSet<T>,
        firstValue: T
    ) {
        val firstEdges = graph.valueToEdges[firstValue]!!
        for (edge in firstEdges) {
            if (edge.destinationValue == firstValue) {
                continue
            }

            valueToExplicitEdge[edge.destinationValue] = edge.toExplicitEdge(firstValue)
        }

        mutableGraph.addVertex(firstValue)
        valuesToBeAdded.remove(firstValue)
        valuesAlreadyInTree.add(firstValue)
    }

    private fun <T> addValueToTree(
        mutableGraph: MutableGraph<T>,
        valuesToBeAdded: MutableSet<T>,
        valuesAlreadyInTree: MutableSet<T>,
        explicitEdge: ExplicitEdge<T>
    ) {
        mutableGraph.addVertex(explicitEdge.destinationValue)
        mutableGraph.addEdge(explicitEdge)
        valuesToBeAdded.remove(explicitEdge.destinationValue)
        valuesAlreadyInTree.add(explicitEdge.destinationValue)
    }

    private fun <T> addCheaperEdges(
        graph: Graph<T>,
        valueToExplicitEdge: MutableMap<T, ExplicitEdge<T>>,
        valuesAlreadyInTree: MutableSet<T>,
        explicitEdge: ExplicitEdge<T>
    ) {
        val newEdges = graph.valueToEdges[explicitEdge.destinationValue]!!
        for (edge in newEdges) {
            if (edge.destinationValue in valuesAlreadyInTree) {
                continue
            }

            val previousWeight = valueToExplicitEdge[edge.destinationValue]?.weight ?: Int.MAX_VALUE
            if (edge.weight < previousWeight) {
                valueToExplicitEdge[edge.destinationValue] = edge.toExplicitEdge(explicitEdge.destinationValue)
            }
        }
    }

    private fun <T> removeInvalidDestinationEdges(
        valueToExplicitEdge: MutableMap<T, ExplicitEdge<T>>,
        destinationValue: T
    ) {
        val valuesToCheck = valueToExplicitEdge.keys.toSet()
        for (value in valuesToCheck) {
            if (value == destinationValue) {
                valueToExplicitEdge.remove(value)
            }
        }
    }
}