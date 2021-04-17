package com.eipipuz.solver


object ShortestSubstringSolver {
    fun findSubstring(string: String, set: Set<Char>): Pair<Int, Int>? {
        if (set.size > string.length) return null
        if (set.isEmpty()) return Pair(0, 0)

        val mapCharToCount = mutableMapOf<Char, Int>()
        var startIndex = 0
        var endIndex = 0
        var minLength = Int.MAX_VALUE
        var result: Pair<Int, Int>? = null

        while (endIndex <= string.lastIndex) {
            val char = string[endIndex]
            if (char in set) {
                val count = mapCharToCount[char] ?: 0
                mapCharToCount[char] = count + 1
            }

            if (mapCharToCount.size == set.size) {
                do {
                    val otherChar = string[startIndex]
                    if (otherChar in set) {
                        val count = mapCharToCount[otherChar] ?: 0
                        if (count == 1) {
                            mapCharToCount.remove(otherChar)
                        } else {
                            mapCharToCount[otherChar] = count - 1
                        }
                    }

                    val length = endIndex - startIndex
                    if (minLength > length) {
                        minLength = length
                        result = Pair(startIndex, endIndex)
                    }

                    startIndex++
                } while (mapCharToCount.size == set.size)
            } else {
                endIndex++
            }
        }

        return result
    }
}