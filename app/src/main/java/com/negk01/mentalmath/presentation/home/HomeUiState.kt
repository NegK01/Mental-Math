package com.negk01.mentalmath.presentation.home

import com.negk01.mentalmath.domain.model.GameRecord

data class HomeUiState(
    val dailyStreak: Int = 0,
    val recentRecords: List<GameRecord> = emptyList()
)
