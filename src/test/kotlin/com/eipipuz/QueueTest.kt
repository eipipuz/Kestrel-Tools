package com.eipipuz

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class QueueTest {
    @Test
    fun testToList() {
        val queue = Queue<Int>().apply {
            enqueue(1)
            enqueue(2)
            dequeue()
            enqueue(3)
        }

        assertEquals(listOf(2, 3), queue.toList())
    }

    @Test
    fun testToListWithVararg() {
        val queue = Queue(1, 3)

        assertEquals(listOf(1, 3), queue.toList())
    }

    @Test
    fun testIsEmpty() {
        assertTrue(Queue<String>().isEmpty())
        assertFalse(Queue("value").isEmpty())
    }
}