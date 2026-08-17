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
        val previous = _uiState.value.selectedDifficulty
        _uiState.update { it.copy(selectedDifficulty = difficulty) }
        saveSettings(_uiState.value.toAppSettings(), onFailure = {
            _uiState.update { it.copy(selectedDifficulty = previous) }
        })
    }

    fun onSoundEnabledChanged(enabled: Boolean) {
        val previous = _uiState.value.soundEnabled
        _uiState.update { it.copy(soundEnabled = enabled) }
        saveSettings(_uiState.value.toAppSettings(), onFailure = {
            _uiState.update { it.copy(soundEnabled = previous) }
        })
    }

    fun onThemePreferenceSelected(themePreference: ThemePreference) {
        val previous = _uiState.value.themePreference
        _uiState.update { it.copy(themePreference = themePreference) }
        saveSettings(_uiState.value.toAppSettings(), onFailure = {
            _uiState.update { it.copy(themePreference = previous) }
        })
    }

    fun onLanguagePreferenceSelected(languagePreference: LanguagePreference) {
        _uiState.update { it.copy(languagePreference = languagePreference) }
        saveSettings(_uiState.value.toAppSettings(), onFailure = {
            _uiState.update { it.copy(languagePreference = LanguagePreference.SYSTEM) }
        })
    }

    fun showDeleteHistoryDialog() {
        _uiState.update { it.copy(showDeleteHistoryDialog = true) }
    }

    fun hideDeleteHistoryDialog() {
        _uiState.update { it.copy(showDeleteHistoryDialog = false) }
    }

    private fun ConfigUiState.toAppSettings(): AppSettings = AppSettings(
        selectedDifficulty = selectedDifficulty,
        soundEnabled = soundEnabled,
        themePreference = themePreference,
        languagePreference = languagePreference,
        hasSeenOnboarding = hasSeenOnboarding
    )

    private fun saveSettings(settings: AppSettings, onFailure: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                settingsRepository.saveSettings(settings)
            } catch (e: Exception) {
                Log.w("ConfigViewModel", "Failed to save settings", e)
                onFailure?.invoke()
            }
        }
    }

    fun clearScoresHistory() {
        viewModelScope.launch {
            try {
                gameRecordRepository.clearAll()
            } catch (e: Exception) {
                Log.w("ConfigViewModel", "Failed to clear scores history", e)
            } finally {
                _uiState.update { it.copy(showDeleteHistoryDialog = false) }
            }
        }
    }
}
