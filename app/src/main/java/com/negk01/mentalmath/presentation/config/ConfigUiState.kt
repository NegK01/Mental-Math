package com.negk01.mentalmath.presentation.config

import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.domain.model.LanguagePreference
import com.negk01.mentalmath.domain.model.ThemePreference

data class ConfigUiState(
    val selectedDifficulty: Difficulty = Difficulty.MEDIUM,
    val soundEnabled: Boolean = true,
    val themePreference: ThemePreference = ThemePreference.SYSTEM,
    val languagePreference: LanguagePreference = LanguagePreference.SYSTEM,
    val hasSeenOnboarding: Boolean = false,
    val showDeleteHistoryDialog: Boolean = false,
)
