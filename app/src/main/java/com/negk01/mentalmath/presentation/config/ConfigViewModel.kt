package com.negk01.mentalmath.presentation.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.negk01.mentalmath.domain.model.AppSettings
import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.domain.model.LanguagePreference
import com.negk01.mentalmath.domain.model.ThemePreference
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
                soundEnabled = settings.soundEnabled,
                themePreference = settings.themePreference,
                languagePreference = settings.languagePreference
            )
        }
    }

    fun onDifficultySelected(difficulty: Difficulty) {
        _uiState.value = _uiState.value.copy(selectedDifficulty = difficulty)
        saveSettings()
    }

    fun onSoundEnabledChanged(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(soundEnabled = enabled)
        saveSettings()
    }

    fun onThemePreferenceSelected(themePreference: ThemePreference) {
        _uiState.value = _uiState.value.copy(themePreference = themePreference)
        saveSettings()
    }

    fun onLanguagePreferenceSelected(languagePreference: LanguagePreference) {
        _uiState.value = _uiState.value.copy(languagePreference = languagePreference)
        saveSettings()
    }

    fun showDeleteHistoryDialog() {
        _uiState.value = _uiState.value.copy(showDeleteHistoryDialog = true)
    }

    fun hideDeleteHistoryDialog() {
        _uiState.value = _uiState.value.copy(showDeleteHistoryDialog = false)
    }

    private fun saveSettings() {
        viewModelScope.launch {
            val hasSeenOnboarding = settingsRepository.getSettings().hasSeenOnboarding
            settingsRepository.saveSettings(
                AppSettings(
                    selectedDifficulty = _uiState.value.selectedDifficulty,
                    soundEnabled = _uiState.value.soundEnabled,
                    themePreference = _uiState.value.themePreference,
                    languagePreference = _uiState.value.languagePreference,
                    hasSeenOnboarding = hasSeenOnboarding
                )
            )
        }
    }

    fun clearScoresHistory() {
        viewModelScope.launch {
            gameRecordRepository.clearAll()
            _uiState.value = _uiState.value.copy(showDeleteHistoryDialog = false)
        }
    }
}
