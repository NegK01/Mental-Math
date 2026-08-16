package com.negk01.mentalmath.domain.model

data class GameRecord(
    val id: Int = 0,
    val playedAt: Long,
    val difficulty: Difficulty,
    val correctAnswers: Int,
    val totalRounds: Int,
    val averageResponseTimeMillis: Long,
    val maxStreak: Int
) {
    /** Convenience accessor — format to UI seconds at the boundary, millis in DB/domain. */
    val averageResponseTimeSeconds: Double get() = averageResponseTimeMillis / 1000.0

    companion object {
        fun fromCompletedSession(
            difficulty: Difficulty,
            correctAnswers: Int,
            totalRounds: Int,
            averageResponseTimeMillis: Long,
            maxStreak: Int,
            playedAt: Long = System.currentTimeMillis()
        ): GameRecord = GameRecord(
            playedAt = playedAt,
            difficulty = difficulty,
            correctAnswers = correctAnswers,
            totalRounds = totalRounds,
            averageResponseTimeMillis = averageResponseTimeMillis,
            maxStreak = maxStreak
        )
    }
}
