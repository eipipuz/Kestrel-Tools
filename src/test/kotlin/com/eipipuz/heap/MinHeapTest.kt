package com.eipipuz.heap

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue


class MinHeapTest {
    @Test
    fun testEmptyHeap() {
        val heap = MinHeap.createEmpty<Int>()

        assertEquals(0, heap.size)
        assertTrue(heap.isEmpty)
        assertNull(heap.min)
    }

    @Test
    fun testFromCollection() {
        val heap = MinHeap.from(1, 6, 3, 5, 8, 4, 7, 2)

        assertEquals(mutableListOf(1, 2, 3, 5, 8, 4, 7, 6), heap.items)
    }

    @Test
    fun testPush() {
        val heap = MinHeap.createEmpty<Int>().apply {
            push(1)
            push(6)
            push(3)
            push(5)
            push(8)
            push(4)
            push(7)
            push(2)
        }

        assertEquals(mutableListOf(1, 2, 3, 5, 8, 4, 7, 6), heap.items)
    }

    @Test
    fun testPop() {
        val heap = MinHeap.from(1, 6, 3, 5, 8, 4, 7, 2)

        val list = mutableListOf<Int>()
        while (!heap.isEmpty) {
            list.add(heap.pop()!!)
        }

        assertEquals(mutableListOf(1, 2, 3, 4, 5, 6, 7, 8), list)
        assertNull(heap.pop())
    }
}