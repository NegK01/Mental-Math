package com.negk01.mentalmath.presentation.results

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.negk01.mentalmath.domain.game.DeltaState
import com.negk01.mentalmath.domain.game.ResultsAnalyzer
import com.negk01.mentalmath.domain.game.ScoreFeedback
import com.negk01.mentalmath.domain.model.CompletionStatus
import com.negk01.mentalmath.domain.model.GameSessionResult
import com.negk01.mentalmath.domain.repository.GameRecordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultsViewModel(
    private val gameRecordRepository: GameRecordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResultsUiState())
    val uiState: StateFlow<ResultsUiState> = _uiState.asStateFlow()

    fun loadForSession(sessionResult: GameSessionResult) {
        _uiState.update {
            ResultsUiState(
                maxStreak = sessionResult.maxStreak,
                delta = DeltaState.None,
                nextGoal = ResultsAnalyzer.computeNextGoal(
                    sessionResult.correctAnswers,
                    sessionResult.difficulty,
                    sessionResult.totalRounds
                ),
                operatorInsight = ResultsAnalyzer.computeOperatorInsight(sessionResult.roundResults),
                scoreFeedback = ResultsAnalyzer.computeScoreFeedback(
                    sessionResult.correctAnswers,
                    sessionResult.totalRounds
                )
            )
        }

        if (sessionResult.completionStatus == CompletionStatus.ABANDONED) {
            return
        }

        viewModelScope.launch {
            val previousBest = try {
                gameRecordRepository.getPreviousBestRecordForDifficulty(sessionResult.difficulty)
            } catch (e: Exception) {
                Log.w("ResultsViewModel", "Failed to load previous best record for ${sessionResult.difficulty}", e)
                null
            }
            _uiState.update {
                it.copy(delta = ResultsAnalyzer.calculateDelta(sessionResult, previousBest))
            }
        }
    }
}
