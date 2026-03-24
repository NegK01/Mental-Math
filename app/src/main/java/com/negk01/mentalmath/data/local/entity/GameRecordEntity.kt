package com.negk01.mentalmath.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_records")
data class GameRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val playedAt: Long,
    val difficulty: String,
    val correctAnswers: Int,
    val totalRounds: Int,
    val averageResponseTimeMillis: Long,
    val maxStreak: Int
)