package com.eipipuz

// Based on: https://gist.github.com/olegcherr/b62a09aba1bff643a049

object BenchMark {
    /**
     * Used to filter console messages easily
     */
    private val buffer = mutableListOf<String>()
    var isVerbose = false

    /**
     * Iterates provided by [callback] code [iterations]x[testCount] times.
     * Performs warming by iterating [iterations]x[warmCount] times.
     */
    fun simpleMeasureTest(
        iterations: Int = 1000,
        testCount: Int = 10,
        warmCount: Int = 2,
        callback: () -> Unit
    ): Pair<Long, Long> {
        val results = ArrayList<Long>()
        var totalTime = 0L
        var t = 0

        while (++t <= testCount + warmCount) {
            val startTime = System.currentTimeMillis()

            var i = 0
            while (i++ < iterations) {
                callback()
            }

            if (t <= warmCount) {
                log { "Warming $t of $warmCount" }
                continue
            }

            val time = System.currentTimeMillis() - startTime
            log { "$time ms" }

            results.add(time)
            totalTime += time
        }

        results.sort()

        val average = totalTime / testCount
        val median = results[results.size / 2]

        log { "average=$average ms / median=$median ms" }

        return Pair(average, median)
    }

    private fun log(messageCallback: () -> String) {
        if (isVerbose) {
            val message = messageCallback()
            println("[BenchMark] $message")
            buffer.add(message)
        }
    }

    fun clear() {
        buffer.clear()
    }
}