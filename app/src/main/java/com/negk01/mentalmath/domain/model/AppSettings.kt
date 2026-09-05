package com.negk01.mentalmath.domain.model

data class AppSettings(
    val languagePreference: LanguagePreference,
    val selectedDifficulty: Difficulty,
    val soundEnabled: Boolean,
    val themePreference: ThemePreference,
    val hasSeenOnboarding: Boolean = false,
    val hasDismissedHomeSupportCard: Boolean = false,
) {
    companion object {
        fun default() = AppSettings(
            languagePreference = LanguagePreference.SYSTEM,
            selectedDifficulty = Difficulty.MEDIUM,
            soundEnabled = true,
            themePreference = ThemePreference.SYSTEM,
            hasSeenOnboarding = false,
            hasDismissedHomeSupportCard = false
        )
    }
}
