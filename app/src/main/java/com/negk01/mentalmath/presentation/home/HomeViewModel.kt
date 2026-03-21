package com.negk01.mentalmath.presentation.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.negk01.mentalmath.domain.model.Score

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeUiState(
            recentScores = listOf(
                Score(
                    difficulty = "Hard",
                    date = "18/3/2026",
                    result = "3/3",
                    time = "0.0s"
                ),
                Score(
                    difficulty = "Hard",
                    date = "18/3/2026",
                    result = "4/5",
                    time = "3.1s"
                )
            )
        )
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
}