package com.eipipuz.solver

import com.eipipuz.MultiSet


object ShortestSubstringSolver {
    fun findSubstring(string: String, set: Set<Char>): Pair<Int, Int>? {
        if (set.size > string.length || string.isEmpty()) return null
        if (set.isEmpty()) return Pair(0, 0)

        val charMultiSet = MultiSet<Char>()
        var startIndex = 0
        var endIndex = 0
        var minLength = Int.MAX_VALUE
        var result: Pair<Int, Int>? = null

        while (endIndex <= string.lastIndex) {
            val char = string[endIndex]
            if (char in set) {
                charMultiSet.add(char)
            }

            if (charMultiSet.size == set.size) {
                do {
                    val otherChar = string[startIndex]
                    if (otherChar in set) {
                        charMultiSet.remove(otherChar)
                    }

                    val length = endIndex - startIndex
                    if (minLength > length) {
                        minLength = length
                        result = Pair(startIndex, endIndex)
                    }

                    startIndex++
                } while (charMultiSet.size == set.size)
            } else {
                endIndex++
            }
        }

        return result
    }
}