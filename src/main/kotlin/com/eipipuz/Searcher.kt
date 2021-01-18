package com.eipipuz


object Searcher {
    internal fun <T : Comparable<T>> binarySearchIndex(list: List<T>, searchedElement: T): Pair<Int, Boolean> {
        if (list.isEmpty()) return Pair(0, false)
        if (list.first().compareTo(searchedElement) == 1) return Pair(0, false)
        if (list.last().compareTo(searchedElement) == -1) return Pair(list.size, false)

        var lowerIndex = 0
        var upperIndex = list.lastIndex

        while (true) {
            val middleIndex: Int = lowerIndex + (upperIndex - lowerIndex) / 2

            when (list[middleIndex].compareTo(searchedElement)) {
                -1 -> {
                    if (lowerIndex == middleIndex) {
                        // This happens when lower and upper are apart by 1.
                        when (list[upperIndex].compareTo(searchedElement)) {
                            -1 -> return Pair(lowerIndex, false)
                            0 -> return Pair(upperIndex, true)
                            1 -> return Pair(upperIndex, false)
                        }
                    } else {
                        lowerIndex = middleIndex
                    }
                }
                0 -> return Pair(middleIndex, true)
                1 -> {
                    upperIndex = middleIndex
                }
            }
        }
    }
}