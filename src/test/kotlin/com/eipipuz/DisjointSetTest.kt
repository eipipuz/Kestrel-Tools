package com.eipipuz

import org.junit.Test
import kotlin.test.assertEquals


class DisjointSetTest {
    @Test
    fun testCreate() {
        val disjointSet = DisjointSet.create(1)

        assertEquals(disjointSet, disjointSet.findParent(), "It should be its own parent")
        assertEquals(1, disjointSet.exemplar)
    }

    @Test
    fun testFindParent() {
        val disjointSet1 = DisjointSet.create(1)
        val disjointSet3 = disjointSet1.addValue(2).addValue(3)

        assertEquals(disjointSet1, disjointSet3.findParent())
    }

    @Test
    fun testUnion() {
        val disjointSet1 = DisjointSet.create(1)
        val disjointSet2 = DisjointSet.create(2).addValue(3)

        val gotDisjointSet = disjointSet2.union(disjointSet1)
        val gotParent = gotDisjointSet.findParent()
        assertEquals(disjointSet2, gotDisjointSet)
        assertEquals(disjointSet2, gotParent)
        assertEquals(disjointSet2, disjointSet2.findParent())
        assertEquals(disjointSet2, disjointSet1.findParent())
    }
}