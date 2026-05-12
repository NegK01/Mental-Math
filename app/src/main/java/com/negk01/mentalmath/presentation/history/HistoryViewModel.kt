package com.negk01.mentalmath.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.negk01.mentalmath.domain.repository.GameRecordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val gameRecordRepository: GameRecordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    private val _shouldScrollToTop = MutableStateFlow(false)
    val shouldScrollToTop: StateFlow<Boolean> = _shouldScrollToTop.asStateFlow()

    private val pageSize = 5

    init {
        viewModelScope.launch { observeStats() }
        viewModelScope.launch { observeNewRecords() }
        viewModelScope.launch { loadPage() }
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
                        records.map { it.averageResponseTimeMillis / 1000.0 }.average()
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
            .collect { resetDisplay(reloadFromDb = true) }
    }

    fun loadMore() {
        if (_uiState.value.isLoadingMore || !_uiState.value.hasMore) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            loadPage()
        }
    }

    // Toca el tab de History estando ya en él → solo scroll, sin resetear datos
    fun onTabReselected() {
        _shouldScrollToTop.value = true
    }

    // Llamado desde AppNavigation cuando el usuario abandona un juego sin guardar.
    // En ese caso observeNewRecords() no dispara (totalGames no cambia),
    // por lo que necesitamos reset explícito desde afuera.
    fun resetToTop() {
        resetDisplay()
    }

    fun consumeScrollToTop() {
        _shouldScrollToTop.value = false
    }

    private fun resetDisplay(reloadFromDb: Boolean = false) {
        val current = _uiState.value.displayRecords
        if (!reloadFromDb && current.isNotEmpty()) {
            val firstPage = current.take(pageSize)
            _uiState.update { it.copy(
                displayRecords = firstPage,
                hasMore = firstPage.size == pageSize,
                isLoadingMore = false
            ) }
            _shouldScrollToTop.value = true
        } else {
            _uiState.update { it.copy(displayRecords = emptyList(), hasMore = false, isLoadingMore = false) }
            _shouldScrollToTop.value = true
            viewModelScope.launch { loadPage() }
        }
    }

    private suspend fun loadPage() {
        val offset = _uiState.value.displayRecords.size

        val newRecords = gameRecordRepository.getRecordsPaged(
            limit = pageSize,
            offset = offset
        )

        _uiState.update { current ->
            current.copy(
                displayRecords = current.displayRecords + newRecords,
                hasMore = newRecords.size == pageSize,
                isLoadingMore = false
            )
        }
    }
}