package com.negk01.mentalmath.presentation.history

import com.negk01.mentalmath.domain.model.GameRecord

data class HistoryUiState(
    val records: List<GameRecord> = emptyList()
)