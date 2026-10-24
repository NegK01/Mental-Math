package com.negk01.mentalmath.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder_progress")
data class ReminderProgressEntity(
    @PrimaryKey val id: Int = 1,
    val lastPlayedAt: Long,
    val stagePriority: Int
)
