package com.negk01.mentalmath.domain.model

data class RoundResult(
    val roundNumber: Int,
    val question: String,
    val userAnswer: Int,
    val correctAnswer: Int,
    val isCorrect: Boolean,
    val timeSpentSeconds: Int,
    val operator: Operator
)