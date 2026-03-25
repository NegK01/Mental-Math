package com.negk01.mentalmath.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.negk01.mentalmath.domain.repository.GameRecordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val gameRecordRepository: GameRecordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        observeHistory()
    }

    private fun observeHistory() {
        viewModelScope.launch {
            gameRecordRepository.getAllRecords().collect { records ->

                val totalGames = records.size

                val averageAccuracy = if (records.isEmpty()) 0.0 else {
                    records.map {
                        it.correctAnswers.toDouble() / it.totalRounds
                    }.average() * 100
                }

                val averageTimeSeconds = if (records.isEmpty()) 0.0 else {
                    records.map {
                        it.averageResponseTimeMillis / 1000.0
                    }.average()
                }

                //val bestStreak = records.maxOfOrNull { it.maxStreak } ?: 0

                _uiState.value = HistoryUiState(
                    records = records,
                    totalGames = totalGames,
                    averageAccuracy = averageAccuracy,
                    averageTimeSeconds = averageTimeSeconds,
                    //bestStreak = bestStreak
                )
            }
        }
    }
}