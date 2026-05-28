package com.negk01.mentalmath.presentation.results

import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.domain.model.Operator

sealed class DeltaState {
    object NewRecord : DeltaState()
    data class Better(val percent: Int) : DeltaState()
    data class Worse(val percent: Int) : DeltaState()
    object FirstGame : DeltaState()
}

sealed class NextGoalState {
    object LowAccuracy : NextGoalState()
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
    val delta: DeltaState = DeltaState.FirstGame,
    val nextGoal: NextGoalState = NextGoalState.LowAccuracy,
    val operatorInsight: OperatorInsight? = null
)
