package com.negk01.mentalmath.ui.utils

import com.negk01.mentalmath.domain.model.Difficulty

fun Difficulty.toDisplayName(): String {
    return when (this) {
        Difficulty.EASY -> "Fácil"
        Difficulty.MEDIUM -> "Medio"
        Difficulty.HARD -> "Difícil"
    }
}