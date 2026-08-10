package com.negk01.mentalmath.presentation.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.domain.model.GameSessionResult
import com.negk01.mentalmath.domain.model.RoundResult
import com.negk01.mentalmath.domain.repository.GameRecordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.floor

class ResultsViewModel(
    private val repository: GameRecordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResultsUiState())
    val uiState: StateFlow<ResultsUiState> = _uiState.asStateFlow()

    fun loadForSession(sessionResult: GameSessionResult) {
        val accuracy = if (sessionResult.totalRounds == 0) 0.0
        else sessionResult.correctAnswers.toDouble() / sessionResult.totalRounds

        _uiState.value = ResultsUiState(
            maxStreak = sessionResult.maxStreak,
            delta = DeltaState.None,
            nextGoal = computeNextGoal(sessionResult.correctAnswers, sessionResult.difficulty, sessionResult.totalRounds),
            operatorInsight = computeOperatorInsight(sessionResult.roundResults)
        )

        viewModelScope.launch {
            val previousBest = try {
                repository.getPreviousBestRecordForDifficulty(sessionResult.difficulty)
            } catch (e: Exception) {
                null
            }
            _uiState.value = _uiState.value.copy(
                delta = calculateDelta(sessionResult, previousBest)
            )
        }
    }

    private fun calculateDelta(current: GameSessionResult, previousBestRecord: com.negk01.mentalmath.domain.model.GameRecord?): DeltaState {
        if (previousBestRecord == null) return DeltaState.FirstGame

        val currentAccuracy = if (current.totalRounds == 0) 0.0 else current.correctAnswers.toDouble() / current.totalRounds
        val previousAccuracy = if (previousBestRecord.totalRounds == 0) 0.0 else previousBestRecord.correctAnswers.toDouble() / previousBestRecord.totalRounds
        val currentAvgTimeMillis = (current.averageResponseTimeSeconds * 1000).toLong()

        val isHigherAccuracy = currentAccuracy > previousAccuracy && current.correctAnswers > 0
        val isEqualAccuracyFasterTime = currentAccuracy == previousAccuracy && currentAvgTimeMillis < previousBestRecord.averageResponseTimeMillis  && previousBestRecord.correctAnswers > 0

        return if (isHigherAccuracy || isEqualAccuracyFasterTime) DeltaState.NewRecord else DeltaState.None
    }

    private fun computeNextGoal(correctAnswers: Int, difficulty: Difficulty, totalRounds: Int): NextGoalState {
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

    private fun computeOperatorInsight(roundResults: List<RoundResult>): OperatorInsight? {
        if (roundResults.size < 2) return null
        val overallAvg = roundResults.map { it.timeSpentSeconds }.average()
        val worst = roundResults
            .groupBy { it.operator }
            .filter { (_, rounds) -> rounds.size >= 2 }
            .maxByOrNull { (_, rounds) -> rounds.map { it.timeSpentSeconds }.average() }
            ?: return null
        val worstAvg = worst.value.map { it.timeSpentSeconds }.average()
        return if (worstAvg > overallAvg * 1.3) OperatorInsight(worst.key, worstAvg) else null
    }

    private fun Difficulty.next(): Difficulty = when (this) {
        Difficulty.EASY -> Difficulty.MEDIUM
        Difficulty.MEDIUM -> Difficulty.HARD
        Difficulty.HARD -> Difficulty.HARD
    }
}
