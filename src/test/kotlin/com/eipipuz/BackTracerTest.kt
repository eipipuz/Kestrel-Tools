package com.eipipuz

import org.junit.Test
import kotlin.test.*


class BackTracerTest {
    @Test
    fun testEightQueens() {
        val eightQueensSolver = EightQueensSolver()
        val board = eightQueensSolver.findSolution()
        assertNotNull(board)
        println(board.toNiceString())
        assertEquals(8, board.queenNumber)
    }

    @Test
    fun testEightQueenSolverKnowsImpossibleScenarios() {
        val board = Board().withQueenAt(1, 1).withQueenAt(0, 0)
        val eightQueensSolver = EightQueensSolver(board)
        assertEquals(BackTracer.SolutionStatus.IMPOSSIBLE, eightQueensSolver.solutionStatus)
    }

    @Test
    fun testEightQueenSolver() {
        val solver = EightQueensSolver()
        val subStates = solver.subStates.toList()
        printSubStates(subStates)
        assertEquals(64, subStates.size)

        val subState__2_3 = subStates[2 * 8 + 3] as EightQueensSolver
        printSubStates(listOf(subState__2_3))
        assertNull(subState__2_3.solution)
        assertEquals(BackTracer.SolutionStatus.ONGOING, subState__2_3.solutionStatus)

        val board__3_4 = subState__2_3.board
        assertEquals(1, board__3_4.queenNumber)
        assertEquals(1, board__3_4.grid[2][3])

        val subSubStates__2_3 = subState__2_3.subStates.toList()
        printSubStates(subSubStates__2_3)
        // Total - row - column - correlated - anti-related
        assertEquals(64 - 8 - 7 - 5 - 6, subSubStates__2_3.size)
    }

    private fun printSubStates(subStates: List<BackTracer<Board>>) {
        val allBoards = subStates
            .filterIsInstance<EightQueensSolver>()
            .map { it.board }
            .reduce { acc, board -> acc + board }
        println("SubState")
        println("----")
        println(allBoards.toNiceString()+"\n")
    }

    @Test
    fun testBoard() {
        val emptyBoard = Board()
        val oneQueenBoard = emptyBoard.withQueenAt(2, 3)
        val multipleQueenBoard = oneQueenBoard.withQueenAt(1, 1)

        assertEquals(emptyList(), emptyBoard.find { it == 1 }.toList())
        assertTrue(oneQueenBoard.canAttackFrom(4, 3), "vertical")
        assertTrue(oneQueenBoard.canAttackFrom(2, 7), "horizontal")
        assertTrue(oneQueenBoard.canAttackFrom(3, 2), "positive correlated")
        assertTrue(oneQueenBoard.canAttackFrom(4, 1), "negative correlated")
        assertTrue(oneQueenBoard.canAttackFrom(4, 5), "positive antirelated")
        assertTrue(oneQueenBoard.canAttackFrom(0, 1), "negative antirelated")
        assertFalse(oneQueenBoard.canAttackFrom(2, 3), "clear")
        assertEquals(listOf(Pair(1, 1), Pair(2, 3)), multipleQueenBoard.find { it == 1 }.toList())
        assertEquals(0, emptyBoard.queenNumber)
        assertEquals(2, multipleQueenBoard.queenNumber)
        assertEquals(
            """
            00000000
            01000000
            00010000
            00000000
            00000000
            00000000
            00000000
            00000000
        """.trimIndent(), multipleQueenBoard.toNiceString()
        )
    }
}