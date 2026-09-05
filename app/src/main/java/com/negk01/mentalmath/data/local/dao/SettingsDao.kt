package com.negk01.mentalmath.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.negk01.mentalmath.data.local.entity.SettingsEntity

@Dao
interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(settings: SettingsEntity)

    @Query("SELECT * FROM settings WHERE id = 1")
    suspend fun getSettings(): SettingsEntity?

    @Query("UPDATE settings SET hasSeenOnboarding = 1 WHERE id = 1")
    suspend fun markOnboardingShown()

    @Query("UPDATE settings SET hasDismissedHomeSupportCard = 1 WHERE id = 1")
    suspend fun markHomeSupportCardDismissed()
}