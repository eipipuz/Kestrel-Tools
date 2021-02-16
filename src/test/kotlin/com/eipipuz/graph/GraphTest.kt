package com.eipipuz.graph

import org.junit.Test
import kotlin.test.assertEquals


class GraphTest {

    private fun assertGraph(
        graph: Graph<Char>,
        expectedIsDirected: Boolean,
        expectedNumEdges: Int,
        expectedValues: Set<Char>
    ) {
        assertEquals(expectedIsDirected, graph.isDirected)
        assertEquals(expectedNumEdges, graph.numEdges, "Number of edges")
        assertEquals(expectedValues, graph.valueToEdges.keys.toSet(), "Values")
    }

    @Test
    fun testCreation() {
        val singleVertex = graph<Char> {
            vertex('a')
        }

        assertGraph(singleVertex, true, 0, setOf('a'))
    }

    @Test
    fun test2Vertices() {
        val twoVertices = graph<Char> {
            vertex('a') {
                vertex('b')
            }
        }

        assertGraph(twoVertices, true, 1, setOf('a', 'b'))
    }

    @Test
    fun test2VerticesInLoop() {
        val twoVertices = graph<Char> {
            vertex('a') {
                vertex('b') {
                    reference('a')
                }
            }
        }

        assertGraph(twoVertices, true, 2, setOf('a', 'b'))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvalidLoopWith2Vertices() {
        graph<Char>(isDirected = false) {
            vertex('a') {
                vertex('b') {
                    reference('a')
                }
            }
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvalidRepeatedVertex() {
        graph<Char> {
            vertex('a') {
                vertex('b')
                vertex('b')
            }
        }
    }

    @Test(expected = IllegalStateException::class)
    fun testBareReference() {
        graph<Char> {
            reference('a')
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUnknownReference() {
        graph<Char> {
            vertex('a') {
                reference('b')
            }
        }
    }

    @Test
    fun test1VertexWithLoop() {
        val vertexAndLoop = graph<Char> {
            vertex('a') {
                reference('a')
            }
        }

        assertGraph(vertexAndLoop, true, 1, setOf('a'))
    }

    @Test
    fun testDisconnectedGraph() {
        val twoVertices = graph<Char> {
            vertex('a')
            vertex('b')
        }

        assertGraph(twoVertices, true, 0, setOf('a', 'b'))
    }

    @Test
    fun test2VerticesNonDirected() {
        val nonDirected = graph<Char>(isDirected = false) {
            vertex('a') {
                vertex('b')
            }
        }

        assertGraph(nonDirected, false, 2, setOf('a', 'b'))
    }

    @Test
    fun test3VerticesNonDirected() {
        val nonDirected = graph<Char>(isDirected = false) {
            vertex('a') {
                vertex('b') {
                    vertex('c') {
                        reference('a')
                    }
                }
            }
        }

        assertGraph(nonDirected, false, 6, setOf('a', 'b', 'c'))
    }

    @Test
    fun testDiamond() {
        val twoVertices = graph<Char> {
            vertex('a') {
                vertex('b') {
                    vertex('c')
                }
                vertex('d') {
                    reference('c')
                }
            }
        }

        assertGraph(twoVertices, true, 4, setOf('a', 'b', 'c', 'd'))
    }

    private val jobGraph = graph<Int> {
        vertex(5) {
            vertex(11) {
                vertex(2)
                vertex(9)
                vertex(10)
            }
        }
        vertex(7) {
            reference(11)
            vertex(8) {
                reference(9)
            }
        }
        vertex(3) {
            reference(8)
            reference(10)
        }
    }

    @Test
    fun testInitialValues() {
        assertEquals(setOf(5, 7, 3), jobGraph.initialValues)
    }

    @Test
    fun testInvertedGraph() {
        val invertedGraph = graph<Int> {
            vertex(2) {
                vertex(11) {
                    vertex(5)
                    vertex(7)
                }
            }
            vertex(9) {
                reference(11)
                vertex(8) {
                    reference(7)
                    vertex(3)
                }
            }
            vertex(10) {
                reference(11)
                reference(3)
            }
        }

        val gotGraph = jobGraph.toInvertedGraph()
        assertEquals(invertedGraph, gotGraph)
    }
}

