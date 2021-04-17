package com.eipipuz.solver

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.fail


class ShortestSubstringSolverTest {
    @Test
    fun testHappyPath() {
        val string = "this is a test"

        val (startIndex, endIndex) = ShortestSubstringSolver.findSubstring(string, setOf('a', 't', 'i'))
            ?: fail("Didn't find answer")
        assertEquals("is a t", string.substring(startIndex, endIndex + 1))
    }

    @Test
    fun testImpossiblyLongSet() {
        val result = ShortestSubstringSolver.findSubstring("at", setOf('a', 't', 'i'))

        assertNull(result)
    }

    @Test
    fun testSetWithUnknownChars() {
        val result = ShortestSubstringSolver.findSubstring("ABCDEF", setOf('B', 'Z'))

        assertNull(result)
    }

    @Test
    fun testTrivialSet() {
        val result = ShortestSubstringSolver.findSubstring("complicated example", emptySet())

        assertEquals(Pair(0, 0), result)
    }
}