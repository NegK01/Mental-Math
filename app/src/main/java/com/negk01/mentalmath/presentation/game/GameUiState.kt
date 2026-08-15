package com.negk01.mentalmath.presentation.game

import com.negk01.mentalmath.domain.model.GameSessionResult

enum class TimerUrgency { NORMAL, WARNING, CRITICAL }

data class GameUiState(
    val currentRound: Int = 1,
    val totalRounds: Int = 6,
    val timeLeftSeconds: Int = 30,
    val questionText: String = "",
    val answerInput: String = "",
    val isPaused: Boolean = false,
    val isFinished: Boolean = false,
    val lastAnswerCorrect: Boolean? = null,
    val isInputLocked: Boolean = false,
    val sessionResult: GameSessionResult? = null
) {
    val timerUrgency: TimerUrgency get() = when {
        timeLeftSeconds <= 5  -> TimerUrgency.CRITICAL
        timeLeftSeconds <= 10 -> TimerUrgency.WARNING
        else                  -> TimerUrgency.NORMAL
    }
}