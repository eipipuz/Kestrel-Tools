package com.eipipuz.graph

interface GraphObserver<T> {
    fun onVertexFound(value: T)

    fun onEdge(sourceValue: T, destinationValue: T, weight: Int)

    fun afterVertexFound(value: T)
}

open class SimpleGraphObserver<T> : GraphObserver<T> {
    override fun onVertexFound(value: T) {}

    override fun onEdge(sourceValue: T, destinationValue: T, weight: Int) {}

    override fun afterVertexFound(value: T) {}
}