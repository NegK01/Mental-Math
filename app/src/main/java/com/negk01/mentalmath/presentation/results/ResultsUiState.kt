package com.negk01.mentalmath.presentation.results

import com.negk01.mentalmath.domain.game.DeltaState
import com.negk01.mentalmath.domain.game.NextGoalState
import com.negk01.mentalmath.domain.game.OperatorInsight
import com.negk01.mentalmath.domain.game.ScoreFeedback

data class ResultsUiState(
    val maxStreak: Int = 0,
    val delta: DeltaState = DeltaState.None,
    val nextGoal: NextGoalState = NextGoalState.LowAccuracy(0, 0),
    val operatorInsight: OperatorInsight? = null,
    val scoreFeedback: ScoreFeedback = ScoreFeedback.KeepPracticing
)
