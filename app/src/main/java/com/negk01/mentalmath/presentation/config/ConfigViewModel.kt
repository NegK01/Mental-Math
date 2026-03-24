package com.negk01.mentalmath.presentation.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.negk01.mentalmath.domain.model.AppSettings
import com.negk01.mentalmath.domain.repository.GameRecordRepository
import com.negk01.mentalmath.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConfigViewModel(
    private val settingsRepository: SettingsRepository,
    private val gameRecordRepository: GameRecordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfigUiState())
    val uiState: StateFlow<ConfigUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings()
            _uiState.value = ConfigUiState(
                selectedDifficulty = settings.selectedDifficulty,
                soundEnabled = settings.soundEnabled
            )
        }
    }

    fun onDifficultySelected(difficulty: String) {
        _uiState.value = _uiState.value.copy(selectedDifficulty = difficulty)
        saveSettings()
    }

    fun onSoundEnabledChanged(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(soundEnabled = enabled)
        saveSettings()
    }

    private fun saveSettings() {
        viewModelScope.launch {
            settingsRepository.saveSettings(
                AppSettings(
                    selectedDifficulty = _uiState.value.selectedDifficulty,
                    soundEnabled = _uiState.value.soundEnabled
                )
            )
        }
    }

    fun clearScoresHistory() {
        viewModelScope.launch {
            gameRecordRepository.clearAll()
        }
    }
}