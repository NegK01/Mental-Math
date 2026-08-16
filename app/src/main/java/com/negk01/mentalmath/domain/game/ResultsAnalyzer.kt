package com.negk01.mentalmath.domain.game

import com.negk01.mentalmath.domain.model.CompletionStatus
import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.domain.model.GameRecord
import com.negk01.mentalmath.domain.model.GameSessionResult
import com.negk01.mentalmath.domain.model.Operator
import com.negk01.mentalmath.domain.model.RoundResult
import kotlin.math.floor

sealed class DeltaState {
    object NewRecord : DeltaState()
    object FirstGame : DeltaState()
    object None : DeltaState()
}

sealed class ScoreFeedback {
    object Great : ScoreFeedback()
    object Good : ScoreFeedback()
    object KeepPracticing : ScoreFeedback()
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

object ResultsAnalyzer {

    fun calculateDelta(current: GameSessionResult, previousBest: GameRecord?): DeltaState {
        if (current.completionStatus == CompletionStatus.ABANDONED) return DeltaState.None
        if (previousBest == null) return DeltaState.FirstGame
        if (current.correctAnswers == 0) return DeltaState.None

        val currentAccuracy = current.correctAnswers.toFloat() / current.totalRounds
        val previousAccuracy = previousBest.correctAnswers.toFloat() / previousBest.totalRounds

        val isHigherAccuracy = currentAccuracy > previousAccuracy
        val isFasterTime = currentAccuracy == previousAccuracy
            && current.averageResponseTimeMillis < previousBest.averageResponseTimeMillis

        return if (isHigherAccuracy || isFasterTime) DeltaState.NewRecord else DeltaState.None
    }

    fun computeNextGoal(correctAnswers: Int, difficulty: Difficulty, totalRounds: Int): NextGoalState {
        val targetCorrect = floor(totalRounds * 0.7).toInt()
        val accuracy = if (totalRounds == 0) 0f else correctAnswers.toFloat() / totalRounds
        return when {
            correctAnswers < targetCorrect -> NextGoalState.LowAccuracy(
                targetCorrect = targetCorrect,
                totalRounds = totalRounds
            )
            difficulty == Difficulty.HARD && accuracy < 1.0f -> NextGoalState.HardKeep
            difficulty == Difficulty.HARD -> NextGoalState.HardPerfect
            accuracy >= 1.0f -> NextGoalState.PerfectNext(difficulty.next())
            else -> NextGoalState.StepUp(difficulty.next())
        }
    }

    fun computeOperatorInsight(roundResults: List<RoundResult>): OperatorInsight? {
        if (roundResults.size < 2) return null
        val overallAvgMillis = roundResults.map { it.timeSpentMillis }.average()
        val worst = roundResults
            .groupBy { it.operator }
            .filter { (_, rounds) -> rounds.size >= 2 }
            .maxByOrNull { (_, rounds) -> rounds.map { it.timeSpentMillis }.average() }
            ?: return null
        val worstAvgMillis = worst.value.map { it.timeSpentMillis }.average()
        return if (worstAvgMillis > overallAvgMillis * 1.3) {
            OperatorInsight(worst.key, worstAvgMillis / 1000.0)
        } else {
            null
        }
    }

    fun computeScoreFeedback(correctAnswers: Int, totalRounds: Int): ScoreFeedback {
        val accuracy = if (totalRounds == 0) 0f else correctAnswers.toFloat() / totalRounds
        return when {
            accuracy >= 0.9f -> ScoreFeedback.Great
            accuracy >= 0.6f -> ScoreFeedback.Good
            else             -> ScoreFeedback.KeepPracticing
        }
    }
}
