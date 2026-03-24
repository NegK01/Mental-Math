
package com.negk01.mentalmath.ui.utils

import com.negk01.mentalmath.domain.model.Difficulty

fun Difficulty.toDisplayName(): String {
    return when (this) {
        Difficulty.EASY -> "Fácil"
        Difficulty.MEDIUM -> "Medio"
        Difficulty.HARD -> "Difícil"
    }
}

fun mapDifficultyLabelToDomain(value: String): Difficulty {
    return when (value.lowercase()) {
        "fácil" -> Difficulty.EASY
        "medio" -> Difficulty.MEDIUM
        "difícil" -> Difficulty.HARD
        else -> Difficulty.MEDIUM
    }
}