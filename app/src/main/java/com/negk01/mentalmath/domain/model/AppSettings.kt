package com.negk01.mentalmath.domain.model

data class AppSettings(
    val selectedDifficulty: Difficulty,
    val soundEnabled: Boolean,
    val themePreference: ThemePreference,
    val languagePreference: LanguagePreference,
    val hasSeenOnboarding: Boolean = false,
) {
    companion object {
        fun default() = AppSettings(
            selectedDifficulty = Difficulty.MEDIUM,
            soundEnabled = true,
            themePreference = ThemePreference.SYSTEM,
            languagePreference = LanguagePreference.SYSTEM,
            hasSeenOnboarding = false
        )
    }
}
