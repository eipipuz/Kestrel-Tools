package com.eipipuz

interface BackTracer<State> {
    val solutionStatus: SolutionStatus
    val solution: State?

    val subStates: Sequence<BackTracer<State>>

    private fun findSolutionState(): BackTracer<State>? {
        if (solutionStatus == SolutionStatus.FOUND) return this

        exploration@ for (subState in subStates) {
            when (subState.solutionStatus) {
                SolutionStatus.FOUND -> return subState
                SolutionStatus.IMPOSSIBLE -> continue@exploration
                SolutionStatus.ONGOING -> Unit
            }

            subState.findSolutionState()?.let {
                when (it.solutionStatus) {
                    SolutionStatus.FOUND -> return it
                    SolutionStatus.IMPOSSIBLE -> throw IllegalStateException("Somehow finished with an impossible solution")
                    SolutionStatus.ONGOING -> throw IllegalStateException("Somehow finished with an ongoing solution")
                }
            }
        }

        return null
    }

    fun findSolution(): State? = findSolutionState()?.solution

    enum class SolutionStatus {
        FOUND, IMPOSSIBLE, ONGOING
    }
}

