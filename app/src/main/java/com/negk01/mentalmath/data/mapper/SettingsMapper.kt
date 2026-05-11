package com.negk01.mentalmath.data.mapper

import com.negk01.mentalmath.data.local.entity.SettingsEntity
import com.negk01.mentalmath.domain.model.AppSettings

fun SettingsEntity.toDomain(): AppSettings {
    return AppSettings(
        selectedDifficulty = selectedDifficulty.toDifficulty(),
        soundEnabled = soundEnabled,
        themePreference = themePreference.toThemePreference(),
        languagePreference = languagePreference.toLanguagePreference(),
        hasSeenOnboarding = hasSeenOnboarding
    )
}

fun AppSettings.toEntity(): SettingsEntity {
    return SettingsEntity(
        id = 1,
        selectedDifficulty = selectedDifficulty.toStorageKey(),
        soundEnabled = soundEnabled,
        themePreference = themePreference.toStorageKey(),
        languagePreference = languagePreference.toStorageKey(),
        hasSeenOnboarding = hasSeenOnboarding
    )
}
