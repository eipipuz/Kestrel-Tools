package com.eipipuz

data class Board(
    val grid: List<List<Int>> = List(8) { List(8) { 0 } }
) {
    val queenNumber = grid.sumBy { row -> row.sum() }

    fun find(condition: (Int) -> Boolean) = sequence {
        grid.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, value ->
                if (condition(value)) {
                    yield(Pair(rowIndex, colIndex))
                }
            }
        }
    }

    fun canAttackFrom(row: Int, col: Int): Boolean {
        val hasQueenInRow = grid[row].filterIndexed { colIndex, _ -> colIndex != col }
            .any { it == 1 }
        if (hasQueenInRow) {
            return true
        }

        val hasQueenInColumn = grid.filterIndexed { rowIndex, _ -> rowIndex != row }
            .any { it[col] == 1 }
        if (hasQueenInColumn) {
            return true
        }

        for (otherRow in 0 until 8) {
            if (otherRow == row) continue

            val delta = row - otherRow
            val correlatedCol = col + delta
            if (correlatedCol in 0 until 8 && grid[otherRow][correlatedCol] == 1) {
                return true
            }

            val antiRelatedCol = col - delta
            if (antiRelatedCol in 0 until 8 && grid[otherRow][antiRelatedCol] == 1) {
                return true
            }
        }

        return false
    }

    fun withQueenAt(row: Int, col: Int): Board {
        val newGrid: MutableList<MutableList<Int>> = grid.map { it.toMutableList() }
            .toMutableList()
        newGrid[row][col] = 1

        return Board(newGrid)
    }

    fun toNiceString(): String = grid.joinToString("\n") { row ->
        row.joinToString("") { it.toString() }
    }

    operator fun plus(other: Board): Board {
        val newGrid: List<List<Int>> = grid.mapIndexed { rowIndex, row ->
            val otherRow = other.grid[rowIndex]

            row.zip(otherRow)
                .map { (value, otherValue) -> if (value + otherValue > 0) 1 else 0 }
        }

        return Board(newGrid)
    }
}