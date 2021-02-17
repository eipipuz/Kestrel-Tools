package com.eipipuz.graph

fun <T> graph(isDirected: Boolean = true, callback: GraphCallback<T>): Graph<T> = GraphBuilder<T>(isDirected)
    .apply(callback)
    .build()

class GraphBuilder<T>(private val isDirected: Boolean) {
    private val valueToEdges = mutableMapOf<T, MutableSet<EdgeToVertex<T>>>()
    private var numEdges = 0
    private var baseValue: T? = null

    var weight: Int = 1

    fun vertex(value: T, callback: GraphCallback<T> = {}) {
        require(value !in valueToEdges.keys) {
            "Value($value) can't be added to graph twice as a vertex. Try using [reference]."
        }

        val previousValue = baseValue
        val previousWeight = weight

        baseValue = value
        valueToEdges[value] = mutableSetOf()

        previousValue?.let {
            addEdge(it, value, weight)

            if (!isDirected) {
                addEdge(value, it, weight)
            }
        }

        callback()

        weight = previousWeight
        baseValue = previousValue
    }

    fun reference(toValue: T) {
        val fromValue = baseValue ?: throw IllegalStateException("There's no base vertex")
        if (toValue !in valueToEdges.keys) {
            throw IllegalArgumentException("Unknown reference($toValue). [vertex] needs to be called beforehand.")
        }

        val edges = valueToEdges[fromValue] ?: emptyList()
        if (edges.any { it.otherValue == toValue }) {
            throw IllegalArgumentException("There's already an edge for value($toValue)")
        }

        addEdge(fromValue, toValue, weight)

        if (!isDirected) {
            addEdge(toValue, fromValue, weight)
        }
    }

    private fun addEdge(fromValue: T, toValue: T, weight: Int) {
        if (weight <= 0) throw IllegalStateException("Weight($weight) needs to be positive")

        val edges = valueToEdges[fromValue]!!
        edges.add(EdgeToVertex(toValue, weight))

        numEdges++
    }

    fun build(): Graph<T> = Graph(valueToEdges, numEdges, isDirected)
}

typealias GraphCallback<T> = GraphBuilder<T>.() -> Unit