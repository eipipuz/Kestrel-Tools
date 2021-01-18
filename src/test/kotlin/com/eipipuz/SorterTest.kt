package com.eipipuz

import org.junit.Test
import kotlin.test.Ignore
import kotlin.test.assertEquals


class SorterTest {
    companion object {
        val list = listOf(2, 7, 8, 2, 1, 0)
    }

    @Test
    fun testQuickSort() {
        assertEquals(listOf(0, 1, 2, 2, 7, 8), Sorter.quickSort(list))
    }

    @Test
    @Ignore
    fun testHeapSort() {
        assertEquals(listOf(0, 1, 2, 2, 7, 8), Sorter.heapSort(list))
    }

    @Test
    fun testInsertSort() {
        assertEquals(listOf(0, 1, 2, 2, 7, 8), Sorter.insertSort(list))
    }

    @Test
    @Ignore
    fun testMergeSort() {
        assertEquals(listOf(0, 1, 2, 2, 7, 8), Sorter.mergeSort(list))
    }
}