package com.negk01.mentalmath.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.negk01.mentalmath.data.local.entity.ReminderProgressEntity

@Dao
interface ReminderProgressDao {
    @Query("SELECT * FROM reminder_progress WHERE id = 1")
    suspend fun getProgress(): ReminderProgressEntity?

    @Upsert
    suspend fun saveProgress(progress: ReminderProgressEntity)
}
