package com.negk01.mentalmath.presentation.game

import com.negk01.mentalmath.domain.model.Difficulty

data class GameUiState(
    val difficulty: Difficulty = Difficulty.EASY,
    val currentRound: Int = 1,
    val totalRounds: Int = 6,
    val timeLeftSeconds: Int = 30,
    val questionText: String = "",
    val answerInput: String = "",
    val isPaused: Boolean = false,
    val isFinished: Boolean = false,
    val lastAnswerCorrect: Boolean? = null,
    val isInputLocked: Boolean = false
)