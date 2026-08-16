package com.negk01.mentalmath.presentation.config

import android.util.Log
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
import kotlinx.coroutines.flow.update
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
            try {
                val settings = settingsRepository.getSettings()
                _uiState.update {
                    it.copy(
                        selectedDifficulty = settings.selectedDifficulty,
                        soundEnabled = settings.soundEnabled,
                        themePreference = settings.themePreference,
                        languagePreference = settings.languagePreference,
                        hasSeenOnboarding = settings.hasSeenOnboarding
                    )
                }
            } catch (e: Exception) {
                Log.w("ConfigViewModel", "Failed to load settings", e)
            }
        }
    }

    fun onDifficultySelected(difficulty: Difficulty) {
        _uiState.update { it.copy(selectedDifficulty = difficulty) }
        saveSettings()
    }

    fun onSoundEnabledChanged(enabled: Boolean) {
        _uiState.update { it.copy(soundEnabled = enabled) }
        saveSettings()
    }

    fun onThemePreferenceSelected(themePreference: ThemePreference) {
        _uiState.update { it.copy(themePreference = themePreference) }
        saveSettings()
    }

    fun onLanguagePreferenceSelected(languagePreference: LanguagePreference) {
        _uiState.update { it.copy(languagePreference = languagePreference) }
        saveSettings()
    }

    fun showDeleteHistoryDialog() {
        _uiState.update { it.copy(showDeleteHistoryDialog = true) }
    }

    fun hideDeleteHistoryDialog() {
        _uiState.update { it.copy(showDeleteHistoryDialog = false) }
    }

    private fun saveSettings() {
        viewModelScope.launch {
            try {
                val current = _uiState.value
                settingsRepository.saveSettings(
                    AppSettings(
                        selectedDifficulty = current.selectedDifficulty,
                        soundEnabled = current.soundEnabled,
                        themePreference = current.themePreference,
                        languagePreference = current.languagePreference,
                        hasSeenOnboarding = current.hasSeenOnboarding
                    )
                )
            } catch (e: Exception) {
                Log.w("ConfigViewModel", "Failed to save settings", e)
            }
        }
    }

    fun clearScoresHistory() {
        viewModelScope.launch {
            try {
                gameRecordRepository.clearAll()
                _uiState.update { it.copy(showDeleteHistoryDialog = false) }
            } catch (e: Exception) {
                Log.w("ConfigViewModel", "Failed to clear scores history", e)
            }
        }
    }
}
