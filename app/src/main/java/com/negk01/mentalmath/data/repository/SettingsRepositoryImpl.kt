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
        return settingsDao.getSettings()?.toDomain() ?: AppSettings.default()
    }

    override suspend fun saveSettings(settings: AppSettings) {
        settingsDao.insertOrUpdate(settings.toEntity())
    }

    override suspend fun markOnboardingShown() {
        val current = settingsDao.getSettings()
        if (current == null) {
            settingsDao.insertOrUpdate(AppSettings.default().copy(hasSeenOnboarding = true).toEntity())
        } else {
            settingsDao.markOnboardingShown()
        }
    }
}
