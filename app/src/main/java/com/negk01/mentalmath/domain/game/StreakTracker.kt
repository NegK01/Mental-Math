package com.negk01.mentalmath.domain.game

class StreakTracker {
    var currentStreak = 0
        private set
    var maxStreak = 0
        private set

    fun reset() {
        currentStreak = 0
        maxStreak = 0
    }

    fun recordAnswer(isCorrect: Boolean) {
        if (isCorrect) {
            currentStreak++
            if (currentStreak > maxStreak) {
                maxStreak = currentStreak
            }
        } else {
            currentStreak = 0
        }
    }
}
