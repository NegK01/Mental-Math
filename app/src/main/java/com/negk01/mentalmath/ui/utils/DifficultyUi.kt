package com.negk01.mentalmath.ui.utils

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.ui.theme.EasyBadgeDark
import com.negk01.mentalmath.ui.theme.EasyBadgeLight
import com.negk01.mentalmath.ui.theme.HardBadgeDark
import com.negk01.mentalmath.ui.theme.HardBadgeLight
import com.negk01.mentalmath.ui.theme.MediumBadgeDark
import com.negk01.mentalmath.ui.theme.MediumBadgeLight

data class DifficultyBadgeColors(
    val container: Color,
    val content: Color
)

fun Difficulty.toStorageKey(): String {
    return when (this) {
        Difficulty.EASY -> "easy"
        Difficulty.MEDIUM -> "medium"
        Difficulty.HARD -> "hard"
    }
}

fun String.toDifficulty(): Difficulty {
    return when (lowercase()) {
        "easy" -> Difficulty.EASY
        "hard" -> Difficulty.HARD
        else -> Difficulty.MEDIUM
    }
}

@StringRes
fun Difficulty.toLabelResId(): Int {
    return when (this) {
        Difficulty.EASY -> R.string.difficulty_easy
        Difficulty.MEDIUM -> R.string.difficulty_medium
        Difficulty.HARD -> R.string.difficulty_hard
    }
}

fun Difficulty.badgeColors(isDarkTheme: Boolean): DifficultyBadgeColors {
    return when (this) {
        Difficulty.EASY -> {
            if (isDarkTheme) {
                DifficultyBadgeColors(
                    container = EasyBadgeDark.copy(alpha = 0.24f),
                    content = EasyBadgeDark
                )
            } else {
                DifficultyBadgeColors(
                    container = EasyBadgeLight.copy(alpha = 0.18f),
                    content = EasyBadgeLight
                )
            }
        }

        Difficulty.MEDIUM -> {
            if (isDarkTheme) {
                DifficultyBadgeColors(
                    container = MediumBadgeDark.copy(alpha = 0.24f),
                    content = MediumBadgeDark
                )
            } else {
                DifficultyBadgeColors(
                    container = MediumBadgeLight.copy(alpha = 0.18f),
                    content = MediumBadgeLight
                )
            }
        }

        Difficulty.HARD -> {
            if (isDarkTheme) {
                DifficultyBadgeColors(
                    container = HardBadgeDark.copy(alpha = 0.24f),
                    content = HardBadgeDark
                )
            } else {
                DifficultyBadgeColors(
                    container = HardBadgeLight.copy(alpha = 0.18f),
                    content = HardBadgeLight
                )
            }
        }
    }
}

@Composable
fun Difficulty.badgeColors(): DifficultyBadgeColors {
    val isDarkTheme = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    return badgeColors(isDarkTheme = isDarkTheme)
}
