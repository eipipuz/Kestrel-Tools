package com.eipipuz

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue


class HeapTest {
    @Test
    fun testEmptyHeap() {
        val heap = Heap.createEmpty<Int>()

        assertEquals(0, heap.size)
        assertTrue(heap.isEmpty)
        assertNull(heap.max)
    }

    @Test
    fun testFromCollection() {
        val heap = Heap.from(1, 6, 3, 5, 8, 4, 7, 2)

        assertEquals(mutableListOf(8, 6, 7, 5, 1, 4, 3, 2), heap.items)
    }

    @Test
    fun testPush() {
        val heap = Heap.createEmpty<Int>().apply {
            push(1)
            push(6)
            push(3)
            push(5)
            push(8)
            push(4)
            push(7)
            push(2)
        }

        assertEquals(mutableListOf(8, 6, 7, 2, 5, 3, 4, 1), heap.items)
    }

    @Test
    fun testPop() {
        val heap = Heap.from(1, 6, 3, 5, 8, 4, 7, 2)

        val list = mutableListOf<Int>()
        while (!heap.isEmpty) {
            list.add(heap.pop()!!)
        }

        assertEquals(mutableListOf(8, 7, 6, 5, 4, 3, 2, 1), list)
        assertNull(heap.pop())
    }
}