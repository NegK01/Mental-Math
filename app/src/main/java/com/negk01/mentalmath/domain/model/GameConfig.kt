package com.negk01.mentalmath.domain.model

data class GameConfig(
    val difficulty: Difficulty,
    val totalRounds: Int,
    val timePerRoundSeconds: Int
)

fun getGameConfig(difficulty: Difficulty): GameConfig {
    return when (difficulty) {
        Difficulty.EASY -> GameConfig(
            difficulty = difficulty,
            totalRounds = 6,
            timePerRoundSeconds = 30
        )

        Difficulty.MEDIUM -> GameConfig(
            difficulty = difficulty,
            totalRounds = 12,
            timePerRoundSeconds = 20
        )

        Difficulty.HARD -> GameConfig(
            difficulty = difficulty,
            totalRounds = 18,
            timePerRoundSeconds = 15
        )
    }
}