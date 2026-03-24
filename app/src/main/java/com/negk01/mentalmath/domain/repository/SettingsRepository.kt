package com.negk01.mentalmath.domain.repository

import com.negk01.mentalmath.domain.model.AppSettings

interface SettingsRepository {
    suspend fun getSettings(): AppSettings
    suspend fun saveSettings(settings: AppSettings)
}