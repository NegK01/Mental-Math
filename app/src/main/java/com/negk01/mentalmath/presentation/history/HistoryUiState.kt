package com.negk01.mentalmath.presentation.history

import com.negk01.mentalmath.domain.model.GameRecord

data class HistoryUiState(
    val records: List<GameRecord> = emptyList(),
    val totalGames: Int = 0,
    val averageAccuracy: Double = 0.0,
    val averageTimeSeconds: Double = 0.0,
)
