package com.negk01.mentalmath.presentation.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.domain.repository.GameRecordRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val gameRecordRepository: GameRecordRepository
) : ViewModel() {

    companion object {
        const val PAGE_SIZE = 5
    }

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    private val _scrollToTopEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val scrollToTopEvent: SharedFlow<Unit> = _scrollToTopEvent.asSharedFlow()

    init {
        viewModelScope.launch { observeStats() }
        viewModelScope.launch { observeNewRecords() }
        viewModelScope.launch { loadPage() }
        viewModelScope.launch { loadBestRecords() }
    }

    private suspend fun observeStats() {
        gameRecordRepository.getAllRecords().collect { records ->
            _uiState.update { current ->
                current.copy(
                    totalGames = records.size,
                    averageAccuracy = if (records.isEmpty()) 0.0 else
                        records.map { it.correctAnswers.toDouble() / it.totalRounds }
                            .average() * 100,
                    averageTimeSeconds = if (records.isEmpty()) 0.0 else
                        records.map { it.averageResponseTimeSeconds }.average()
                )
            }
        }
    }

    // drop(1) salta la emisión inicial — solo reacciona a inserts reales
    private suspend fun observeNewRecords() {
        gameRecordRepository.getAllRecords()
            .map { it.size }
            .distinctUntilChanged()
            .drop(1)
            .collect {
                resetDisplay(reloadFromDb = true)
                loadBestRecords()
            }
    }

    private suspend fun loadBestRecords() {
        val bestEasy = try {
            gameRecordRepository.getBestRecordForDifficulty(Difficulty.EASY)
        } catch (e: Exception) {
            Log.w("HistoryViewModel", "Failed to load best record for EASY", e)
            null
        }
        val bestMedium = try {
            gameRecordRepository.getBestRecordForDifficulty(Difficulty.MEDIUM)
        } catch (e: Exception) {
            Log.w("HistoryViewModel", "Failed to load best record for MEDIUM", e)
            null
        }
        val bestHard = try {
            gameRecordRepository.getBestRecordForDifficulty(Difficulty.HARD)
        } catch (e: Exception) {
            Log.w("HistoryViewModel", "Failed to load best record for HARD", e)
            null
        }
        _uiState.update { it.copy(bestEasyRecord = bestEasy, bestMediumRecord = bestMedium, bestHardRecord = bestHard) }
    }

    fun loadMore() {
        if (_uiState.value.isLoadingMore || !_uiState.value.hasMore) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            loadPage()
        }
    }

    // Toca el tab de History estando ya en él → emite evento de scroll
    fun onTabReselected() {
        _scrollToTopEvent.tryEmit(Unit)
    }

    // Llamado desde AppNavigation cuando el usuario abandona un juego sin guardar.
    // En ese caso observeNewRecords() no dispara (totalGames no cambia),
    // por lo que necesitamos reset explícito desde afuera.
    fun resetToTop() {
        resetDisplay()
    }

    private fun resetDisplay(reloadFromDb: Boolean = false) {
        val current = _uiState.value.displayRecords
        if (!reloadFromDb && current.isNotEmpty()) {
            val firstPage = current.take(PAGE_SIZE)
            _uiState.update { it.copy(
                displayRecords = firstPage,
                hasMore = firstPage.size == PAGE_SIZE,
                isLoadingMore = false
            ) }
            _scrollToTopEvent.tryEmit(Unit)
        } else {
            _uiState.update { it.copy(displayRecords = emptyList(), hasMore = false, isLoadingMore = false) }
            _scrollToTopEvent.tryEmit(Unit)
            viewModelScope.launch { loadPage() }
        }
    }

    private suspend fun loadPage() {
        val offset = _uiState.value.displayRecords.size

        val newRecords = gameRecordRepository.getRecordsPaged(
            limit = PAGE_SIZE,
            offset = offset
        )

        _uiState.update { current ->
            current.copy(
                displayRecords = current.displayRecords + newRecords,
                hasMore = newRecords.size == PAGE_SIZE,
                isLoadingMore = false
            )
        }
    }
}