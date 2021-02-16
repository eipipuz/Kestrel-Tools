package com.eipipuz

import org.junit.Test
import kotlin.test.assertEquals


class StackTest {
    @Test
    fun testToList() {
        val stack = Stack<Int>().apply {
            push(1)
            push(2)
            pop()
            push(3)
        }

        assertEquals(listOf(3,1), stack.toList())
    }

    @Test
    fun testToListWithVararg() {
        val stack = Stack(1,3)

        assertEquals(listOf(3,1), stack.toList())
    }
}