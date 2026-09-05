package com.negk01.mentalmath.data.mapper

import com.negk01.mentalmath.data.local.entity.SettingsEntity
import com.negk01.mentalmath.domain.model.AppSettings

fun SettingsEntity.toDomain(): AppSettings {
    return AppSettings(
        languagePreference = languagePreference.toLanguagePreference(),
        selectedDifficulty = selectedDifficulty.toDifficulty(),
        soundEnabled = soundEnabled,
        themePreference = themePreference.toThemePreference(),
        hasSeenOnboarding = hasSeenOnboarding,
        hasDismissedHomeSupportCard = hasDismissedHomeSupportCard
    )
}

fun AppSettings.toEntity(): SettingsEntity {
    return SettingsEntity(
        id = 1,
        languagePreference = languagePreference.toStorageKey(),
        selectedDifficulty = selectedDifficulty.toStorageKey(),
        soundEnabled = soundEnabled,
        themePreference = themePreference.toStorageKey(),
        hasSeenOnboarding = hasSeenOnboarding,
        hasDismissedHomeSupportCard = hasDismissedHomeSupportCard
    )
}
