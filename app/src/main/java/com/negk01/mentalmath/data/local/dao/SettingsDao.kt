package com.negk01.mentalmath.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.negk01.mentalmath.data.local.entity.SettingsEntity

@Dao
interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfMissing(settings: SettingsEntity)

    @Query("UPDATE settings SET languagePreference = :language, selectedDifficulty = :difficulty, soundEnabled = :soundEnabled, themePreference = :theme WHERE id = 1")
    suspend fun updatePreferences(language: String, difficulty: String, soundEnabled: Boolean, theme: String)

    @Query("SELECT * FROM settings WHERE id = 1")
    suspend fun getSettings(): SettingsEntity?

    @Query("UPDATE settings SET hasSeenOnboarding = 1 WHERE id = 1")
    suspend fun markOnboardingShown()

    @Query("UPDATE settings SET hasDismissedHomeSupportCard = 1 WHERE id = 1")
    suspend fun markHomeSupportCardDismissed()
}
