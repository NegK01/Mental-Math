package com.negk01.mentalmath.data.repository

import com.negk01.mentalmath.data.local.dao.SettingsDao
import com.negk01.mentalmath.data.mapper.toDomain
import com.negk01.mentalmath.data.mapper.toEntity
import com.negk01.mentalmath.domain.model.AppSettings
import com.negk01.mentalmath.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val settingsDao: SettingsDao
) : SettingsRepository {

    override suspend fun getSettings(): AppSettings {
        return settingsDao.getSettings()?.toDomain()
            ?: AppSettings(
                selectedDifficulty = "Medio",
                soundEnabled = true
            )
    }

    override suspend fun saveSettings(settings: AppSettings) {
        settingsDao.insertOrUpdate(settings.toEntity())
    }
}