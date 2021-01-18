package com.eipipuz

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MultiSetTest {
    @Test
    fun testAddContainsRemove() {
        val multiSet = MultiSet(1, 2, 2)

        assertTrue(1 in multiSet)
        assertTrue(2 in multiSet)
        assertFalse(0 in multiSet)

        multiSet.remove(1)
        assertFalse(1 in multiSet)

        multiSet.remove(2)
        assertTrue(2 in multiSet)
    }

    @Test
    fun testEquality() {
        val multiSet = MultiSet(12, 42, 12)
        val multiSetWithApply = MultiSet<Int>().apply {
            add(12)
            add(42)
            add(12)
        }
        val multiSetWithList = MultiSet(listOf(12, 42, 12))
        val multiSetClone = multiSet.clone()

        assertEquals(multiSet, multiSetWithApply)
        assertEquals(multiSet, multiSetWithList)
        assertEquals(multiSet, multiSetClone)
    }

    private fun assertIterator(iterator: Iterator<Int>, vararg expected: Int) {
        val list = iterator.asSequence().toList()

        assertEquals(expected.toList(), list)
    }

    @Test
    fun testIterator() {
        val multiSet = MultiSet(1, 2, 3, 3, 2, 3)

        assertIterator(multiSet.iterator(), 1, 2, 2, 3, 3, 3)
        assertIterator(multiSet.iterator().apply {
            remove()
        }, 1, 2, 2, 3, 3)
        assertIterator(multiSet.iterator().apply {
            remove()
            remove()
            remove()
            remove()
        }, 1, 2)
    }

    @Test
    fun testUnionIntersection() {
        val multiSet = MultiSet(2, 1, 2, 1)
        val otherMultiSet = MultiSet(1, 3, 3)
        val unionMultiSet = MultiSet(1, 1, 2, 2, 3, 3)
        val intersectionMultiSet = MultiSet(1)

        assertEquals(unionMultiSet, multiSet.union(otherMultiSet))
        assertEquals(intersectionMultiSet, multiSet.intersection(otherMultiSet))
    }

    @Test
    fun testSizeAndFullSize() {
        val multiSet = MultiSet('a', 'b', 'c', 'a')

        assertEquals(3, multiSet.size)
        assertEquals(4, multiSet.fullSize)
    }

    @Test
    fun testClear() {
        val originalMultiSet = MultiSet('a', 'b', 'c', 'a')
        val clearedMultiSet = originalMultiSet.clone().apply {
            clear()
        }

        assertEquals(3, originalMultiSet.size)
        assertEquals(0, clearedMultiSet.size)

        val otherMultiSet = MultiSet('z', 'z')

        assertEquals(clearedMultiSet, clearedMultiSet.intersection(otherMultiSet))
        assertEquals(otherMultiSet, clearedMultiSet.union(otherMultiSet))
    }
}
