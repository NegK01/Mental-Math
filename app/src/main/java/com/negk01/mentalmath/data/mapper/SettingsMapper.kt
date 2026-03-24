package com.negk01.mentalmath.data.mapper

import com.negk01.mentalmath.data.local.entity.SettingsEntity
import com.negk01.mentalmath.domain.model.AppSettings

fun SettingsEntity.toDomain(): AppSettings {
    return AppSettings(
        selectedDifficulty = selectedDifficulty,
        soundEnabled = soundEnabled
    )
}

fun AppSettings.toEntity(): SettingsEntity {
    return SettingsEntity(
        id = 1,
        selectedDifficulty = selectedDifficulty,
        soundEnabled = soundEnabled
    )
}