package com.negk01.mentalmath.domain.model

data class AppSettings(
    val selectedDifficulty: Difficulty,
    val soundEnabled: Boolean,
    val themePreference: ThemePreference,
    val languagePreference: LanguagePreference,
    val hasSeenOnboarding: Boolean = false,
)
