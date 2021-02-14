package com.eipipuz

fun <T> graph(isDirected: Boolean = true, callback: GraphCallback<T>): Graph<T> = GraphBuilder<T>(isDirected)
    .apply(callback)
    .build()

class GraphBuilder<T>(private val isDirected: Boolean) {
    private val vertexToValue = mutableMapOf<VertexId, T>()
    private val valueToVertexId = mutableMapOf<T, VertexId>()
    private val vertexToEdges = mutableMapOf<VertexId, MutableList<EdgeToVertex>>()
    private var numEdges = 0
    private var baseVertexId: VertexId? = null
    fun vertex(value: T, callback: GraphCallback<T> = {}) {
        require(value !in valueToVertexId) {
            "Value($value) can't be added to graph twice as a vertex. Try using [reference]."
        }

        val previousVertexId = baseVertexId

        val vertexId = VertexId(vertexToValue.size)
        baseVertexId = vertexId
        vertexToValue[vertexId] = value
        valueToVertexId[value] = vertexId
        vertexToEdges[vertexId] = mutableListOf()

        previousVertexId?.let {
            addEdge(it, vertexId, 1)

            if (!isDirected) {
                addEdge(vertexId, it, 1)
            }
        }

        callback()

        baseVertexId = previousVertexId
    }

    fun reference(value: T) {
        val fromVertexId = baseVertexId ?: throw IllegalStateException("There's no base vertex")
        val toVertexId = valueToVertexId[value]
            ?: throw IllegalArgumentException("Unknown reference. [vertex] needs to be called beforehand.")

        val edges = vertexToEdges[fromVertexId] ?: emptyList()
        if (edges.any { it.vertexId == toVertexId }) {
            throw IllegalArgumentException("There's already an edge for value($value)")
        }

        addEdge(fromVertexId, toVertexId, 1)

        if (!isDirected) {
            addEdge(toVertexId, fromVertexId, 1)
        }
    }

    private fun addEdge(fromVertexId: VertexId, toVertexId: VertexId, weight: Int) {
        if (weight <= 0) throw IllegalStateException("Weight($weight) needs to be positive")

        val edges = vertexToEdges[fromVertexId]!!
        edges.add(EdgeToVertex(toVertexId, weight))

        numEdges++
    }

    fun build(): Graph<T> = Graph(vertexToEdges, vertexToValue, valueToVertexId, numEdges, isDirected)
}

typealias GraphCallback<T> = GraphBuilder<T>.() -> Unit

class Graph<T>(
    val vertexIdToEdges: Map<VertexId, List<EdgeToVertex>>,
    val vertexIdToValue: Map<VertexId, T>,
    val valueToVertexId: Map<T, VertexId>,
    val numEdges: Int,
    val isDirected: Boolean
    // TODO: Add isUnweighted
)

data class EdgeToVertex(val vertexId: VertexId, val weight: Int)

inline class VertexId(val int: Int)

interface GraphObserver<T> {
    fun onVertexFound(value: T)

    fun onEdge(fromValue: T, toValue: T, weight: Int)

    fun afterVertexFound(value: T)
}

open class SimpleGraphObserver<T> : GraphObserver<T> {
    override fun onVertexFound(value: T) {}

    override fun onEdge(fromValue: T, toValue: T, weight: Int) {}

    override fun afterVertexFound(value: T) {}
}