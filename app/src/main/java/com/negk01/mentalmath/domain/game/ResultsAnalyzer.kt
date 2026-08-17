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
        if (current.totalRounds == 0 || previousBest.totalRounds == 0) return DeltaState.None
        if (current.correctAnswers == 0) return DeltaState.None

        val currentAccuracy = current.correctAnswers.toFloat() / current.totalRounds
        val previousAccuracy = previousBest.correctAnswers.toFloat() / previousBest.totalRounds

        val isHigherAccuracy = currentAccuracy > previousAccuracy
        val isFasterTime = currentAccuracy == previousAccuracy
            && current.averageResponseTimeMillis < previousBest.averageResponseTimeMillis

        return if (isHigherAccuracy || isFasterTime) DeltaState.NewRecord else DeltaState.None
    }

    fun computeNextGoal(correctAnswers: Int, difficulty: Difficulty, totalRounds: Int): NextGoalState {
        if (totalRounds == 0) return NextGoalState.LowAccuracy(targetCorrect = 1, totalRounds = 0)
        val targetCorrect = floor(totalRounds * 0.7).toInt()
        val accuracy = correctAnswers.toFloat() / totalRounds
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
        val operatorAverages = roundResults
            .groupBy { it.operator }
            .filter { (_, rounds) -> rounds.size >= 2 }
            .mapValues { (_, rounds) -> rounds.map { it.timeSpentMillis }.average() }

        val worstEntry = operatorAverages.maxByOrNull { it.value } ?: return null
        val worstAvgMillis = worstEntry.value

        return if (worstAvgMillis > overallAvgMillis * 1.3) {
            OperatorInsight(worstEntry.key, worstAvgMillis / 1000.0)
        } else {
            null
        }
    }

    fun computeScoreFeedback(correctAnswers: Int, totalRounds: Int): ScoreFeedback {
        if (totalRounds == 0) return ScoreFeedback.KeepPracticing
        val targetCorrect = floor(totalRounds * 0.7).toInt()
        val accuracy = correctAnswers.toFloat() / totalRounds
        return when {
            accuracy >= 0.9f -> ScoreFeedback.Great
            correctAnswers >= targetCorrect -> ScoreFeedback.Good
            else -> ScoreFeedback.KeepPracticing
        }
    }
}
