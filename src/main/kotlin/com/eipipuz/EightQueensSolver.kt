package com.eipipuz

class EightQueensSolver(val board: Board = Board()) : BackTracer<Board> {
    override val solutionStatus: BackTracer.SolutionStatus
        get() {
            val canAttack = board.find { it == 1 }
                .any { (row, col) ->
                    board.canAttackFrom(row, col) }
            if (canAttack) return BackTracer.SolutionStatus.IMPOSSIBLE

            val hasEnoughQueens = board.queenNumber == 8

            return if (hasEnoughQueens) BackTracer.SolutionStatus.FOUND else BackTracer.SolutionStatus.ONGOING
        }

    override val solution: Board?
        get() = if (solutionStatus == BackTracer.SolutionStatus.FOUND) board else null

    override val subStates: Sequence<BackTracer<Board>>
        get() = sequence {
            val options = board.find { it == 0 }
                .mapNotNull { (row, col) ->
                    if (board.canAttackFrom(row, col)) {
                        null
                    } else {
                        board.withQueenAt(row, col)
                    }
                }
                .map { EightQueensSolver(it) }
            yieldAll(options)
        }
}