package com.eipipuz.tree

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class SegmentTreeTest {
    @Test
    fun testInRange() {
        val list = listOf(1, 3, 5, 7, 9, 11)
        val segmentTree = SegmentTree.sumsFrom(list)

        assertEquals(36, segmentTree.op(0..5))
        assertEquals(9, segmentTree.op(0..2))
        assertEquals(24, segmentTree.op(1..4))
        assertEquals(20, segmentTree.op(4..5))
    }

    @Test
    fun testBadList() {
        val gotException = try {
            SegmentTree.sumsFrom(listOf())

            false
        } catch (exception: IllegalArgumentException) {
            true
        }

        assertTrue(gotException)
    }

    @Test
    fun testUpdate() {
        val list = listOf(1, 3, 5, 7, 9, 11)
        val segmentTree = SegmentTree.sumsFrom(list)
        val delta = 2

        segmentTree.update(1, delta)

        assertEquals(36 + delta, segmentTree.op(0..5))
        assertEquals(9 + delta, segmentTree.op(0..2))
        assertEquals(24 + delta, segmentTree.op(1..4))
        assertEquals(20, segmentTree.op(4..5))
    }

    @Test
    fun testUpdateWithBadDelta() {
        val list = listOf(1, 3, 5, 7, 9, 11)
        val segmentTree = SegmentTree.sumsFrom(list)

        val gotException = try {
            segmentTree.update(1, 2.0)

            false
        } catch (exception: IllegalArgumentException) {
            true
        }

        assertTrue(gotException)
    }
}