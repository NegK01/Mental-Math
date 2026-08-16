package com.negk01.mentalmath.domain.model

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD;

    fun next(): Difficulty = when (this) {
        EASY -> MEDIUM
        MEDIUM -> HARD
        HARD -> HARD
    }
}
