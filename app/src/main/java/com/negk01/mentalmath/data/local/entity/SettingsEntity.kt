package com.negk01.mentalmath.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 1,
    val selectedDifficulty: String,
    val soundEnabled: Boolean,
    val themePreference: String,
    val languagePreference: String
)
