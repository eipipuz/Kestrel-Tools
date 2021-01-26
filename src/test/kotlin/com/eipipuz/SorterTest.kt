package com.eipipuz

import org.junit.Test
import kotlin.test.Ignore
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class SorterTest {
    companion object {
        val list = listOf(2, 7, 8, 2, 1, 0)
    }

    @Test
    fun testQuickSort() {
        assertEquals(listOf(0, 1, 2, 2, 7, 8), Sorter.quickSort(list))
    }

    @Test
    fun testHeapSort() {
        assertEquals(listOf(0, 1, 2, 2, 7, 8), Sorter.heapSort(list))
    }

    @Test
    fun testInsertSort() {
        assertEquals(listOf(0, 1, 2, 2, 7, 8), Sorter.insertSort(list))
    }

    @Test
    fun testMergeSort() {
        assertEquals(listOf(0, 1, 2, 2, 7, 8), Sorter.mergeSort(list))
    }

    @Test
    @Ignore
    fun testBenchMark() {
//        BenchMark.isVerbose = true

        val thousandItems = sequence {
            for (i in 1000 downTo 0) {
                yield(i)
            }
        }
            .toList()
        val expect = thousandItems.sorted()
        val (average, median) = BenchMark.simpleMeasureTest {
            assertEquals(expect, Sorter.mergeSort(thousandItems))
        }

        assertTrue(average <= 235 && median <= 235, "Average: $average / Median: $median")
    }
}