package com.negk01.mentalmath.presentation.history

import com.negk01.mentalmath.domain.model.GameRecord

data class HistoryUiState(
    // Métricas globales — sobre todos los registros históricos
    val totalGames: Int = 0,
    val averageAccuracy: Double = 0.0,
    val averageTimeSeconds: Double = 0.0,

    // Lista visual paginada — crece con cada loadMore()
    val displayRecords: List<GameRecord> = emptyList(),

    // true si hay más registros disponibles en DB
    val hasMore: Boolean = false,

    // true mientras loadMore() está en vuelo
    val isLoadingMore: Boolean = false
)