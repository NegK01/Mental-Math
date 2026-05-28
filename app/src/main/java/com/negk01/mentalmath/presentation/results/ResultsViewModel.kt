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
            delta = DeltaState.FirstGame,
            nextGoal = computeNextGoal(accuracy.toFloat(), sessionResult.difficulty),
            operatorInsight = computeOperatorInsight(sessionResult.roundResults)
        )

        viewModelScope.launch {
            val bestAccuracy = try {
                repository.getBestAccuracyForDifficulty(sessionResult.difficulty)
            } catch (e: Exception) {
                null
            }
            _uiState.value = _uiState.value.copy(
                delta = calculateDelta(accuracy, bestAccuracy)
            )
        }
    }

    private fun calculateDelta(current: Double, best: Double?): DeltaState {
        if (best == null) return DeltaState.FirstGame
        return when {
            current >= best -> DeltaState.NewRecord
            else -> {
                val percent = ((best - current) / best * 100).toInt().coerceAtLeast(1)
                DeltaState.Worse(percent)
            }
        }
    }

    private fun computeNextGoal(accuracy: Float, difficulty: Difficulty): NextGoalState {
        return when {
            accuracy < 0.7f -> NextGoalState.LowAccuracy
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
