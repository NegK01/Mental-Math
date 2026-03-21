package com.negk01.mentalmath.presentation.config

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ConfigViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ConfigUiState())
    val uiState: StateFlow<ConfigUiState> = _uiState.asStateFlow()

    fun onDifficultySelected(difficulty: String) {
        _uiState.update { currentState ->
            currentState.copy(selectedDifficulty = difficulty)
        }
    }

    fun onSoundEnabledChanged(enabled: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(soundEnabled = enabled)
        }
    }

    fun clearScoresHistory() {
        // luego conectamos esto con Room
    }
}