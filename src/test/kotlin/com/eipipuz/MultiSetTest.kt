package com.eipipuz

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MultiSetTest {
    @Test
    fun testAddContainsRemove() {
        val multiSet = MultiSet<Int>()

        listOf(1,2,2).forEach {
            multiSet.add(it)
        }

        assertTrue(1 in multiSet)
        assertTrue(2 in multiSet)
        assertFalse(0 in multiSet)

        multiSet.remove(1)
        assertFalse(1 in multiSet)

        multiSet.remove(2)
        assertTrue(2 in multiSet)
    }
}
