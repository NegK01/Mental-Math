package com.negk01.mentalmath.presentation.results

import com.negk01.mentalmath.domain.model.RoundDetail

data class ResultsUiState(
    val correctAnswers: Int = 0,
    val averageTime: String = "--",
    val roundDetails: List<RoundDetail> = emptyList()
)