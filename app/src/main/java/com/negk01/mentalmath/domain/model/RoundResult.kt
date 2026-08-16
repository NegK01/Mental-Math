package com.negk01.mentalmath.domain.model

data class RoundResult(
    val question: String,
    val userAnswer: Int,
    val correctAnswer: Int,
    val isCorrect: Boolean,
    val timeSpentMillis: Long,
    val operator: Operator
) {
    val timeSpentSeconds: Double get() = timeSpentMillis / 1000.0
}