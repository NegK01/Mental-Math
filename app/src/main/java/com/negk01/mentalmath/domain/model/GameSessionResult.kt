package com.negk01.mentalmath.domain.model

data class GameSessionResult(
    val difficulty: Difficulty,
    val correctAnswers: Int,
    val totalRounds: Int,
    val averageResponseTimeSeconds: Double,
    val completionStatus: CompletionStatus,
    val roundResults: List<RoundResult>,
    val maxStreak: Int
)