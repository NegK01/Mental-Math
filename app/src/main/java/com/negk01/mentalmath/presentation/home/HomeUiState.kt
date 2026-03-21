package com.negk01.mentalmath.presentation.home

import com.negk01.mentalmath.domain.model.Score

data class HomeUiState(
    val title: String = "Mental Math",
    val subtitle: String = "Entrena tu agilidad mental",
    val dailyStreak: Int = 0,
    val recentScores: List<Score> = emptyList()
)