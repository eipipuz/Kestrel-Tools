package com.eipipuz

import kotlin.test.*

class CircularBufferTest {
    private lateinit var buffer: CircularBuffer<Int>

    @BeforeTest
    fun setup() {
        buffer = CircularBuffer(3)
    }

    @Test
    fun sizeIsChecked() {
        assertFails { CircularBuffer<Int>(-1) }
    }

    @Test
    fun writesCanBeRead() {
        // Nothing written yet
        assertNull(buffer.read())

        buffer.write(2)
        assertEquals(2, buffer.read())
        // Nothing written yet
        assertNull(buffer.read())

        buffer.write(4)
        buffer.write(8)
        assertEquals(4, buffer.read())
        assertEquals(8, buffer.read())
    }

    @Test
    fun enoughWritesOverwrite() {
        buffer.write(3)
        buffer.write(5)
        buffer.write(7)
        buffer.write(9)

        listOf(5, 7, 9).forEach {
            assertEquals(it, buffer.read())
        }

        assertNull(buffer.read())

        buffer.write(11)
        assertEquals(7, buffer.read())
    }

    @Test
    fun isEmptyAndIsFull() {
        assertTrue(buffer.isEmpty)
        assertFalse(buffer.isFull)

        buffer.write(1)
        assertFalse(buffer.isEmpty)
        assertFalse(buffer.isFull)

        buffer.write(2)
        assertFalse(buffer.isFull)

        buffer.write(3)
        assertTrue(buffer.isFull)

        // Over capacity keeps the semantics
        buffer.write(4)
        assertTrue(buffer.isFull)
        assertFalse(buffer.isEmpty)
    }

    @Test
    fun deletes() {
        buffer.write(1)
        buffer.write(2)
        buffer.write(3)
        assertTrue(buffer.isFull)

        listOf(1, 2, 3).forEach {
            val gotValue = buffer.delete()
            assertEquals(it, gotValue)
        }
        assertFalse(buffer.isFull)
        assertTrue(buffer.isEmpty)
    }
}