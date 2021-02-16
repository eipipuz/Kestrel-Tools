package com.eipipuz.graph

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