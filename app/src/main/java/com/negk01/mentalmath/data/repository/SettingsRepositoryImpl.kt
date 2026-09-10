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
        val current = settingsDao.getSettings()
        val toSave = if (current != null) {
            settings.toEntity().copy(
                hasSeenOnboarding = current.hasSeenOnboarding || settings.hasSeenOnboarding,
                hasDismissedHomeSupportCard = current.hasDismissedHomeSupportCard || settings.hasDismissedHomeSupportCard
            )
        } else {
            settings.toEntity()
        }
        settingsDao.insertOrUpdate(toSave)
    }

    override suspend fun markOnboardingShown() {
        val current = settingsDao.getSettings()
        if (current == null) {
            settingsDao.insertOrUpdate(AppSettings.default().copy(hasSeenOnboarding = true).toEntity())
        } else {
            settingsDao.markOnboardingShown()
        }
    }

    override suspend fun markHomeSupportCardDismissed() {
        val current = settingsDao.getSettings()
        if (current == null) {
            settingsDao.insertOrUpdate(AppSettings.default().copy(hasDismissedHomeSupportCard = true).toEntity())
        } else {
            settingsDao.markHomeSupportCardDismissed()
        }
    }
}
