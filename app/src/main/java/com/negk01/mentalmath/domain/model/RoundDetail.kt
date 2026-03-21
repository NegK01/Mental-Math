package com.negk01.mentalmath.domain.model

data class RoundDetail(
    val expression: String,
    val userAnswer: String,
    val isCorrect: Boolean,
    val time: String
)