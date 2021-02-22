package com.eipipuz.graph

interface AbstractGraph<T> {
    val valueToEdges: Map<T, Set<EdgeToVertex<T>>>
    val numEdges: Int
    val isDirected: Boolean
    // TODO: Add isUnweighted
}

class Graph<T> constructor(
    override val valueToEdges: Map<T, Set<EdgeToVertex<T>>>,
    override val numEdges: Int,
    override val isDirected: Boolean
    // TODO: Add isUnweighted
) : AbstractGraph<T> {
    val initialValues: Set<T> by lazy {
        val receivingValues = valueToEdges.values.flatMap { edges ->
            edges.map { it.destinationValue }
        }.toSet()
        val allValues = valueToEdges.keys


        allValues.minus(receivingValues)
    }

    val valueToIncomingCount: Map<T, Int> by lazy {
        val valueToIncomingCount = mutableMapOf<T, Int>()
        valueToEdges.forEach { (value, edges) ->
            edges.forEach {
                val count = valueToIncomingCount.getOrDefault(it.destinationValue, 0)
                valueToIncomingCount[it.destinationValue] = count + 1
            }

            valueToIncomingCount.getOrPut(value) { 0 } // This way every value is guaranteed to have a number.
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
                val invertedEdges = invertedGraphValueToEdges.getOrDefault(edge.destinationValue, mutableSetOf())
                invertedEdges.add(EdgeToVertex(value, edge.weight))
                invertedGraphValueToEdges[edge.destinationValue] = invertedEdges
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

class MutableGraph<T> constructor(
    override val valueToEdges: MutableMap<T, MutableSet<EdgeToVertex<T>>>,
    override var numEdges: Int,
    override val isDirected: Boolean
    // TODO: Add isUnweighted
) : AbstractGraph<T> {
    companion object {
        fun <T> createEmpty(isDirected: Boolean = true): MutableGraph<T> {
            return MutableGraph(mutableMapOf(), 0, isDirected)
        }
    }

    fun addVertex(value: T): MutableGraph<T> {
        require(value !in valueToEdges) { "Value($value) already part of the mutable graph." }

        valueToEdges[value] = mutableSetOf()

        return this
    }

    fun addEdge(explicitEdge: ExplicitEdge<T>): MutableGraph<T> =
        addEdge(explicitEdge.sourceValue, explicitEdge.destinationValue, explicitEdge.weight)

    fun addEdge(sourceValue: T, destinationValue: T, weight: Int = 1, isDirected: Boolean = true): MutableGraph<T> {
        require(sourceValue in valueToEdges) { "Unknown sourceValue($sourceValue)" }
        require(destinationValue in valueToEdges) { "Unknown destinationValue($destinationValue)" }

        val fromEdges = valueToEdges[sourceValue]!!
        fromEdges.add(EdgeToVertex(destinationValue, weight))
        numEdges++

        if (!isDirected) {
            val toEdges = valueToEdges[destinationValue]!!
            toEdges.add(EdgeToVertex(sourceValue, weight))
            numEdges++
        }

        return this
    }

    fun toGraph(): Graph<T> = Graph(valueToEdges, numEdges, isDirected)
}

data class EdgeToVertex<T>(val destinationValue: T, val weight: Int) {
    fun toExplicitEdge(sourceValue: T): ExplicitEdge<T> = ExplicitEdge(sourceValue, destinationValue, weight)
}

data class ExplicitEdge<T>(val sourceValue: T, val destinationValue: T, val weight: Int)