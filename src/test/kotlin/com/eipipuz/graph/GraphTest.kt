package com.eipipuz.graph

import com.eipipuz.Searcher
import org.junit.Test
import kotlin.test.assertEquals


class GraphTest {

    private fun <T> assertGraph(
        graph: Graph<T>,
        expectedIsDirected: Boolean,
        expectedNumEdges: Int,
        expectedValues: Set<T>
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

    private fun <T> assertWeightsBetweenVertices(graph: Graph<T>, value1: T, value2: T, expectedWeight: Int) {
        val edge1 = graph.valueToEdges[value1]?.find { it.destinationValue == value2 }!!
        assertEquals(expectedWeight, edge1.weight)
        val edge2 = graph.valueToEdges[value2]?.find { it.destinationValue == value1 }!!
        assertEquals(expectedWeight, edge2.weight)
    }

    @Test
    fun testWeightsInGraph() {
        val weightedGraph = graph<Char>(isDirected = false) {
            vertex('a') {
                vertex('b')
            }
            weight = 2
            vertex('c') {
                vertex('d')
            }

            weight = 5
            vertex('e') {
                weight = 3
                vertex('f')
            }
            vertex('g') {
                vertex('h')
            }
        }

        assertWeightsBetweenVertices(weightedGraph, 'a', 'b', 1)
        assertWeightsBetweenVertices(weightedGraph, 'c', 'd', 2)
        assertWeightsBetweenVertices(weightedGraph, 'e', 'f', 3)
        assertWeightsBetweenVertices(weightedGraph, 'g', 'h', 5)
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

    @Test
    fun testPrimsMinimumSpanningTest() {
        val someGraph = graph<Int>(isDirected = false) {
            vertex(0) {
                weight = 4
                vertex(1) {
                    weight = 8
                    vertex(2) {
                        weight = 7
                        vertex(3) {
                            weight = 9
                            vertex(4)
                            weight = 14
                            vertex(5) {
                                weight = 10
                                reference(4)
                                weight = 4
                                reference(2)
                            }
                        }
                        weight = 2
                        vertex(8) {
                            weight = 6
                            vertex(6) {
                                weight = 2
                                reference(5)
                            }
                            weight = 7
                            vertex(7) {
                                weight = 1
                                reference(6)
                                weight = 11
                                reference(1)
                                weight = 8
                                reference(0)
                            }
                        }
                    }
                }
            }
        }

        val gotGraph = Searcher.findPrimsMinimumSpanningTree(someGraph)
        val expectedGraph = graph<Int> {
            vertex(0) {
                weight = 4
                vertex(1)
                weight = 8
                vertex(7) {
                    weight = 1
                    vertex(6) {
                        weight = 2
                        vertex(5) {
                            weight = 4
                            vertex(2) {
                                weight = 2
                                vertex(8)
                                weight = 7
                                vertex(3) {
                                    weight = 9
                                    vertex(4)
                                }
                            }
                        }
                    }
                }
            }
        }

        assertEquals(expectedGraph, gotGraph)
    }
}

