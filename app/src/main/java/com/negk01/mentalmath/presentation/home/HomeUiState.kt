package com.negk01.mentalmath.presentation.home

import com.negk01.mentalmath.domain.model.GameRecord
import java.time.LocalDate

data class HomeUiState(
    val dailyStreak: Int = 0,
    val recentRecords: List<GameRecord> = emptyList(),
    val showOnboarding: Boolean = false,
    val showCalendarDialog: Boolean = false,
    val streakStartDate: LocalDate? = null,
    val streakEndDate: LocalDate? = null
)
