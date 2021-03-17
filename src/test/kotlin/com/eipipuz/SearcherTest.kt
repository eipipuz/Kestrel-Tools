package com.eipipuz

import com.eipipuz.graph.SimpleGraphObserver
import com.eipipuz.graph.graph
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull


class SearcherTest {
    @Test
    fun testEmptyBinarySearch() {
        val (index, found) = Searcher.binarySearchIndex(emptyList(), 42)
        assertEquals(0, index)
        assertFalse(found)
    }

    private fun assert1ElementBinarySearch(
        searchedElement: Int,
        expectedFound: Boolean,
        expectedList: MutableList<Int>
    ) {
        val mutableList = mutableListOf(0)
        val (index, found) = Searcher.binarySearchIndex(mutableList, searchedElement)
        assertEquals(expectedFound, found)
        mutableList.add(index, searchedElement)
        assertEquals(expectedList, mutableList)
    }

    @Test
    fun test1ElementBinarySearch() {
        assert1ElementBinarySearch(42, false, mutableListOf(0, 42))
        assert1ElementBinarySearch(0, true, mutableListOf(0, 0))
        assert1ElementBinarySearch(-42, false, mutableListOf(-42, 0))
    }

    private fun assertBinarySearch(list: List<Int>, element: Int, expectedIndex: Int, expectedFound: Boolean) {
        val (index, found) = Searcher.binarySearchIndex(list, element)
        assertEquals(expectedIndex, index)
        assertEquals(expectedFound, found)
    }

    @Test
    fun testBinarySearch() {
        val list = listOf(2, 3, 5, 7)
        assertBinarySearch(list, -1, 0, false)
        assertBinarySearch(list, 2, 0, true)
        assertBinarySearch(list, 3, 1, true)
        assertBinarySearch(list, 4, 2, false)
        assertBinarySearch(list, 5, 2, true)
        assertBinarySearch(list, 6, 3, false)
        assertBinarySearch(list, 7, 3, true)
        assertBinarySearch(list, 11, 4, false)
    }

    private fun assertQuickSelect(list: List<Int>, nth: Int, expected: Int?) {
        val got = Searcher.quickSelect(list.toMutableList(), nth)

        if (expected != null) {
            assertEquals(expected, got)
        } else {
            assertNull(got)
        }
    }

    @Test
    fun testQuickSelect() {
        val list = listOf(4, 5, 2, 3, 0, 1)

        assertQuickSelect(list, -1, null)
        for (i in 0..5) {
            assertQuickSelect(list, i, i)
        }
        assertQuickSelect(list, 6, null)
    }

    @Test
    fun testQuickSelectMultipleCalls() {
        val list = mutableListOf(4, 5, 2, 3, 0, 1)

        assertQuickSelect(list, -1, null)
        assertQuickSelect(list, 2, 2)
        assertQuickSelect(list, 5, 5)
        assertQuickSelect(list, 6, null)
    }

    @Test
    fun testBFS() {
        val someGraph = graph<Int>(isDirected = false) {
            vertex(1) {
                vertex(2) {
                    vertex(3) {
                        vertex(4) {
                            vertex(5) {
                                reference(2)
                                reference(1)
                            }
                        }
                    }
                }
                vertex(6)
            }
        }

        val gotVertices = mutableListOf<Int>()
        val graphObserver = object : SimpleGraphObserver<Int>() {
            override fun onVertexFound(value: Int) {
                gotVertices.add(value)
            }
        }

        Searcher.breathFirstSearch(someGraph, 1, graphObserver)

        val expectedVertices = listOf(1, 2, 5, 6, 3, 4)
        assertEquals(expectedVertices, gotVertices.toList())
    }

    @Test
    fun testDFS() {
        val someGraph = graph<Int>(isDirected = false) {
            vertex(1) {
                vertex(2) {
                    vertex(3) {
                        vertex(4) {
                            vertex(5) {
                                reference(2)
                                reference(1)
                            }
                        }
                    }
                }
                vertex(6)
            }
        }

        val gotVertices = mutableListOf<Int>()
        val graphObserver = object : SimpleGraphObserver<Int>() {
            override fun onVertexFound(value: Int) {
                gotVertices.add(value)
            }
        }

        Searcher.depthFirstSearch(someGraph, 1, graphObserver)

        val expectedVertices = listOf(1, 2, 3, 4, 5, 6)
        assertEquals(expectedVertices, gotVertices.toList())
    }

    private fun assertLongestCommonSublist(string1: String, string2: String, expectedString: String) {
        val gotSublist = Searcher.findLongestCommonSublist(string1.toList(), string2.toList())
        assertEquals(expectedString, gotSublist.joinToString(""))
    }

    @Test
    fun testFindLongestCommonSublist() {
        assertLongestCommonSublist("Hello world!", "98765", "")
        assertLongestCommonSublist("1234567", "200234056", "234")
    }

    private fun assertMajority(list: List<Int>, expectedElement: Int?, shouldConfirm: Boolean = true) {
        val gotElement = Searcher.findMajorityElement(list, shouldConfirm)

        assertEquals(expectedElement, gotElement)
    }

    @Test
    fun testFindMajority() {
        assertMajority(listOf(), null)
        assertMajority(listOf(5), 5)
        assertMajority(listOf(2, 3), null)
        assertMajority(listOf(11, 4, 11), 11)
        assertMajority(listOf(1, 1, 2, 1, 2, 3, 3, 2, 2, 2, 1, 2, 2, 3, 2, 2), 2)
    }

    @Test
    fun testFindMajorityWithoutConfirmation() {
        assertMajority(listOf(), null, false)
        assertMajority(listOf(5), 5, false)
        assertMajority(listOf(2, 3), 3, false)
        assertMajority(listOf(11, 4, 11), 11, false)
        assertMajority(listOf(11, 4, 4, 11), 4, false)
    }
}