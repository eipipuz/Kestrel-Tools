package com.eipipuz

import com.eipipuz.graph.graph
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
    fun testTopologicalSort() {
        val someGraph = graph<Int> {
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

        val gotValues = Sorter.topologicalSort(someGraph)

        val expectedValues = listOf(7, 5, 11, 3, 10, 8, 9, 2)
        assertEquals(expectedValues, gotValues)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun testTopologicalSortInALoop() {
        val loop = graph<Int> {
            vertex(1) {
                vertex(2) {
                    reference(1)
                }
            }
        }

        Sorter.topologicalSort(loop)
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