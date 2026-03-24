package com.negk01.mentalmath.domain.model

data class GameRecord(
    val id: Int = 0,
    val playedAt: Long,
    val difficulty: String,
    val correctAnswers: Int,
    val totalRounds: Int,
    val averageResponseTimeMillis: Long,
    val maxStreak: Int
)