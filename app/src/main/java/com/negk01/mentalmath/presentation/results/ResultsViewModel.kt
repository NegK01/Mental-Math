package com.negk01.mentalmath.presentation.results

import androidx.lifecycle.ViewModel
import com.negk01.mentalmath.domain.model.RoundDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ResultsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        ResultsUiState(
            correctAnswers = 3,
            averageTime = "1.1s",
            roundDetails = listOf(
                RoundDetail("7 × 3", "21", true, "1.2s"),
                RoundDetail("5 + 8", "12", false, "1.4s"),
                RoundDetail("9 ÷ 3", "3", true, "0.9s"),
                RoundDetail("8 + 6", "14", true, "0.8s")
            )
        )
    )

    val uiState: StateFlow<ResultsUiState> = _uiState.asStateFlow()
}