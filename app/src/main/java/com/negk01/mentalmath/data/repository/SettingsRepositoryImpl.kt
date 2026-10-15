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
        val entity = settings.toEntity()
        settingsDao.insertIfMissing(entity)
        settingsDao.updatePreferences(
            entity.languagePreference,
            entity.selectedDifficulty,
            entity.soundEnabled,
            entity.themePreference
        )
    }

    override suspend fun markOnboardingShown() {
        settingsDao.insertIfMissing(AppSettings.default().toEntity())
        settingsDao.markOnboardingShown()
    }

    override suspend fun markHomeSupportCardDismissed() {
        settingsDao.insertIfMissing(AppSettings.default().toEntity())
        settingsDao.markHomeSupportCardDismissed()
    }
}
