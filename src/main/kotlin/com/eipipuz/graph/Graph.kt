package com.eipipuz.graph

interface AbstractGraph<T> {
    val valueToEdges: Map<T, Set<EdgeToVertex<T>>>
    val numEdges: Int
    val isDirected: Boolean
    // TODO: Add isUnweighted
}

class Graph<T>(
    override val valueToEdges: Map<T, Set<EdgeToVertex<T>>>,
    override val numEdges: Int,
    override val isDirected: Boolean
    // TODO: Add isUnweighted
) : AbstractGraph<T> {
    val initialValues: Set<T> by lazy {
        val receivingValues = valueToEdges.values.flatMap { edges ->
            edges.map { it.otherValue }
        }.toSet()
        val allValues = valueToEdges.keys


        allValues.minus(receivingValues)
    }

    val valueToIncomingCount: Map<T, Int> by lazy {
        val valueToIncomingCount = mutableMapOf<T, Int>()
        valueToEdges.forEach { (value, edges)  ->
            edges.forEach {
                val count = valueToIncomingCount.getOrDefault(it.otherValue, 0)
                valueToIncomingCount[it.otherValue] =  count + 1
            }

            valueToIncomingCount.getOrPut(value ) { 0 } // This way every value is guaranteed to have a number.
        }

        valueToIncomingCount
    }

    fun toMutableGraph(): MutableGraph<T> {
        val valueToEdge = valueToEdges.mapValues { (_, edges) ->
            edges.toMutableSet()
        }
            .toMutableMap()
        return MutableGraph(valueToEdge, numEdges, isDirected)
    }

    fun toInvertedGraph(): Graph<T> {
        val invertedGraphValueToEdges = mutableMapOf<T, MutableSet<EdgeToVertex<T>>>()
        valueToEdges.forEach { (value: T, edges: Set<EdgeToVertex<T>>) ->
            edges.forEach { edge ->
                val invertedEdges = invertedGraphValueToEdges.getOrDefault(edge.otherValue, mutableSetOf())
                invertedEdges.add(EdgeToVertex(value, edge.weight))
                invertedGraphValueToEdges[edge.otherValue] = invertedEdges
            }

            // Needed for initial values. Otherwise the graph will lose knowledge of them.
            if (invertedGraphValueToEdges[value] == null) {
                invertedGraphValueToEdges[value] = mutableSetOf()
            }
        }

        return Graph(invertedGraphValueToEdges, numEdges, isDirected)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Graph<*>

        if (valueToEdges != other.valueToEdges) return false
        if (numEdges != other.numEdges) return false
        if (isDirected != other.isDirected) return false

        return true
    }

    override fun hashCode(): Int {
        var result = valueToEdges.hashCode()
        result = 31 * result + numEdges
        result = 31 * result + isDirected.hashCode()

        return result
    }

    override fun toString(): String {
        return "Graph(valueToEdges=$valueToEdges, numEdges=$numEdges, isDirected=$isDirected)"
    }
}

class MutableGraph<T>(
    override val valueToEdges: MutableMap<T, MutableSet<EdgeToVertex<T>>>,
    override var numEdges: Int,
    override val isDirected: Boolean
    // TODO: Add isUnweighted
) : AbstractGraph<T>

data class EdgeToVertex<T>(val otherValue: T, val weight: Int)