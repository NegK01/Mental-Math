package com.negk01.mentalmath.presentation.results

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.negk01.mentalmath.domain.game.DeltaState
import com.negk01.mentalmath.domain.game.ResultsAnalyzer
import com.negk01.mentalmath.domain.model.CompletionStatus
import com.negk01.mentalmath.domain.model.GameSessionResult
import com.negk01.mentalmath.domain.repository.GameRecordRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
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
    private var loadJob: Job? = null
    private var loadedRecordId: Long? = null
    private val celebratedRecordIds = mutableSetOf<Long>()

    fun loadForSession(sessionResult: GameSessionResult, savedRecordId: Long?) {
        loadJob?.cancel()
        loadedRecordId = null
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

        if (sessionResult.completionStatus == CompletionStatus.ABANDONED || savedRecordId == null) {
            return
        }

        loadJob = viewModelScope.launch {
            val previousBest = try {
                gameRecordRepository.getPreviousBestRecordForDifficulty(sessionResult.difficulty, savedRecordId)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.w("ResultsViewModel", "Failed to load previous best record for ${sessionResult.difficulty}", e)
                return@launch
            }
            currentCoroutineContext().ensureActive()
            loadedRecordId = savedRecordId
            _uiState.update {
                it.copy(delta = ResultsAnalyzer.calculateDelta(sessionResult, previousBest))
            }
        }
    }

    fun consumeRecordCelebration(): Boolean {
        val recordId = loadedRecordId ?: return false
        if (_uiState.value.delta != DeltaState.NewRecord) return false
        return celebratedRecordIds.add(recordId)
    }
}
