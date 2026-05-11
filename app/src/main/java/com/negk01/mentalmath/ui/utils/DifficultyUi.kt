package com.negk01.mentalmath.ui.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.ui.theme.BadgeEasy
import com.negk01.mentalmath.ui.theme.BadgeEasyContainer
import com.negk01.mentalmath.ui.theme.BadgeHard
import com.negk01.mentalmath.ui.theme.BadgeHardContainer
import com.negk01.mentalmath.ui.theme.BadgeMedium
import com.negk01.mentalmath.ui.theme.BadgeMediumContainer
import com.negk01.mentalmath.ui.theme.EasyBadgeLight
import com.negk01.mentalmath.ui.theme.HardBadgeLight
import com.negk01.mentalmath.ui.theme.MediumBadgeLight

data class DifficultyBadgeColors(
    val container: Color,
    val content: Color
)

@StringRes
fun Difficulty.toLabelResId(): Int {
    return when (this) {
        Difficulty.EASY -> R.string.difficulty_easy
        Difficulty.MEDIUM -> R.string.difficulty_medium
        Difficulty.HARD -> R.string.difficulty_hard
    }
}

fun Difficulty.badgeColorsFor(isDarkTheme: Boolean): DifficultyBadgeColors {
    return when (this) {
        Difficulty.EASY -> {
            if (isDarkTheme) {
                DifficultyBadgeColors(container = BadgeEasyContainer, content = BadgeEasy)
            } else {
                DifficultyBadgeColors(
                    container = EasyBadgeLight.copy(alpha = 0.18f),
                    content = EasyBadgeLight
                )
            }
        }

        Difficulty.MEDIUM -> {
            if (isDarkTheme) {
                DifficultyBadgeColors(container = BadgeMediumContainer, content = BadgeMedium)
            } else {
                DifficultyBadgeColors(
                    container = MediumBadgeLight.copy(alpha = 0.18f),
                    content = MediumBadgeLight
                )
            }
        }

        Difficulty.HARD -> {
            if (isDarkTheme) {
                DifficultyBadgeColors(container = BadgeHardContainer, content = BadgeHard)
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
fun Difficulty.badgeColors(): DifficultyBadgeColors = badgeColorsFor(isDarkTheme = isDarkTheme())
