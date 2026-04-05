package com.negk01.mentalmath.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.negk01.mentalmath.domain.model.GameRecord
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
    private var currentOffset = 0
    private val displayRecords = mutableListOf<GameRecord>()

    init {
        viewModelScope.launch { observeStats() }
        viewModelScope.launch { observeNewRecords() }
        viewModelScope.launch { loadPage() }
    }

    // Métricas siempre sobre el total histórico completo
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

    // drop(1) salta la emisión inicial — solo reacciona a inserts reales.
    // Sin lastKnownTotal ni comparaciones manuales de conteos.
    private suspend fun observeNewRecords() {
        gameRecordRepository.getAllRecords()
            .map { it.size }
            .distinctUntilChanged()
            .drop(1)
            .collect { resetDisplay() }
    }

    fun loadMore() {
        if (_uiState.value.isLoadingMore || !_uiState.value.hasMore) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            loadPage()
        }
    }

    // Llamado cuando el usuario toca el tab de History estando ya en History.
    // Solo hace scroll al top — no resetea la lista paginada ni relanza queries.
    fun onTabReselected() {
        _shouldScrollToTop.value = true
    }

    fun consumeScrollToTop() {
        _shouldScrollToTop.value = false
    }

    private fun resetDisplay() {
        displayRecords.clear()
        currentOffset = 0
        _shouldScrollToTop.value = true
        viewModelScope.launch { loadPage() }
    }

    private suspend fun loadPage() {
        val newRecords = gameRecordRepository.getRecordsPaged(
            limit = pageSize,
            offset = currentOffset
        )
        displayRecords.addAll(newRecords)
        currentOffset += newRecords.size

        _uiState.update { current ->
            current.copy(
                displayRecords = displayRecords.toList(),
                hasMore = newRecords.size == pageSize,
                isLoadingMore = false
            )
        }
    }
}