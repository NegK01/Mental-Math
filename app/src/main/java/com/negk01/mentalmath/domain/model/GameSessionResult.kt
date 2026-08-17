package com.negk01.mentalmath.domain.model

data class GameSessionResult(
    val difficulty: Difficulty,
    val correctAnswers: Int,
    val totalRounds: Int,
    val averageResponseTimeMillis: Long,
    val completionStatus: CompletionStatus,
    val roundResults: List<RoundResult>,
    val maxStreak: Int
) {
    val averageResponseTimeSeconds: Double get() = averageResponseTimeMillis / 1000.0

    companion object {
        fun fromRounds(
            difficulty: Difficulty,
            totalRounds: Int,
            completionStatus: CompletionStatus,
            roundResults: List<RoundResult>,
            maxStreak: Int
        ): GameSessionResult = GameSessionResult(
            difficulty = difficulty,
            correctAnswers = roundResults.count { it.isCorrect },
            totalRounds = totalRounds,
            averageResponseTimeMillis = if (roundResults.isEmpty()) {
                0L
            } else {
                Math.round(roundResults.map { it.timeSpentMillis }.average())
            },
            completionStatus = completionStatus,
            roundResults = roundResults,
            maxStreak = maxStreak
        )
    }
}