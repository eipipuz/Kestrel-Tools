package com.eipipuz.tree


class SegmentTree private constructor(
    private val range: IntRange,
    private var sum: Number,
    private val left: SegmentTree?,
    private val right: SegmentTree?,
    private val op: (Number, Number) -> Number
) {
    companion object {
        fun sumsFrom(list: List<Number>) = from(list, ::plus)

        fun from(list: List<Number>, op: (Number, Number) -> Number): SegmentTree {
            require(list.isNotEmpty())

            return from(list, op, 0, list.lastIndex)
        }

        private fun plus(n: Number, m: Number): Number {
            return when (n) {
                is Int -> {
                    m as Int

                    n + m
                }
                else -> throw UnsupportedOperationException()
            }
        }

        private fun from(list: List<Number>, op: (Number, Number) -> Number, start: Int, end: Int): SegmentTree {
            val range = start..end

            return if (start == end) {
                SegmentTree(range, list[start], null, null, op)
            } else {
                val middle = start + (end - start) / 2
                val left = from(list, op, start, middle)
                val right = from(list, op, middle + 1, end)
                val sum = op(left.sum, right.sum)

                SegmentTree(range, sum, left, right, op)
            }
        }
    }

    fun op(range: IntRange): Number {
        return when {
            this.range == range -> sum // This isn't strictly needed.
            range.last < this.range.first || range.first > this.range.last -> 0
            this.range.last == this.range.first && this.range.first in range -> sum
            else -> {
                val leftSum = left?.op(range) ?: 0
                val rightSum = right?.op(range) ?: 0

                op(leftSum, rightSum)
            }
        }
    }

    fun update(at: Int, delta: Number) {
        require(sum::class == delta::class)

        if (at !in range) return

        sum = op(sum, delta)

        left?.update(at, delta)
        right?.update(at, delta)
    }
}