package com.eipipuz

class CircularBuffer<T>(val size: Int) {
    init {
        require(size >= 0) { "Size needs to be positive" }
    }

    private val buffer = MutableList<T?>(size) { null }
    private var writeIndex: Int = 0
    private var readIndex: Int = 0
    private var readCaughtToWrite = true
    private var count: Int = 0

    private fun incIndexWithWrap(value: Int): Int {
        return (value + 1) % size
    }

    private fun incCount() {
        if (count < size) {
            count++
        }
    }

    private fun decCount() {
        if (count > 0) {
            count--
        }
    }

    fun write(item: T) {
        val index = writeIndex
        buffer[index] = item
        if (index == readIndex) { // This does not imply overwriting. At the start we have this position
            if (isFull) { // This is an overwrite.
                readIndex = incIndexWithWrap(index)
            }
            readCaughtToWrite = false
        }
        writeIndex = incIndexWithWrap(index)
        incCount()
    }

    fun delete(): T? {
        val index = readIndex
        val result = buffer[index]
        result?.let {
            buffer[index] = null
            readIndex = incIndexWithWrap(index)
            decCount()
        }

        return result
    }

    fun read(): T? {
        if (readCaughtToWrite) {
            return null
        }

        val index = readIndex
        val result = buffer[index]
        result?.let {
            readIndex = incIndexWithWrap(index)
            if (readIndex == writeIndex) {
                readCaughtToWrite = true
            }
        }

        return result
    }

    val isFull: Boolean
        get() = count == size

    val isEmpty: Boolean
        get() = count == 0
}