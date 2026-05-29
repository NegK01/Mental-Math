package com.negk01.mentalmath.presentation.results

import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.domain.model.Operator

sealed class DeltaState {
    object NewRecord : DeltaState()
    object FirstGame : DeltaState()
    object None : DeltaState()
}

sealed class NextGoalState {
    data class LowAccuracy(val targetCorrect: Int, val totalRounds: Int) : NextGoalState()
    data class StepUp(val nextDifficulty: Difficulty) : NextGoalState()
    object HardKeep : NextGoalState()
    object HardPerfect : NextGoalState()
    data class PerfectNext(val nextDifficulty: Difficulty) : NextGoalState()
}

data class OperatorInsight(
    val operator: Operator,
    val avgSeconds: Double
)

data class ResultsUiState(
    val maxStreak: Int = 0,
    val delta: DeltaState = DeltaState.None,
    val nextGoal: NextGoalState = NextGoalState.LowAccuracy(0, 0),
    val operatorInsight: OperatorInsight? = null
)
