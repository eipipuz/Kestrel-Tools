package com.eipipuz

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
}