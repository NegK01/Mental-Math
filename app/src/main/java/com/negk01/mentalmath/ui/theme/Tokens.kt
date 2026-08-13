package com.negk01.mentalmath.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.ui.unit.dp

object Opacity {
    const val Disabled = 0.38f
    const val Hover = 0.08f
    const val Focus = 0.12f
    const val Pressed = 0.16f
    const val BadgeContainer = 0.18f
    const val Scrim = 0.6f
    const val ScrimSoft = 0.45f
}

object Elevation {
    val None = 0.dp
    val Level1 = 1.dp
    val Level2 = 3.dp
    val Level3 = 6.dp
    val Level4 = 8.dp
}

object Motion {
    const val Fast = 150
    const val Medium = 250
    const val Slow = 400
    const val Celebration = 600
    val EaseStandard = FastOutSlowInEasing
    val EaseEmphasized = CubicBezierEasing(0.2f, 0f, 0f, 1f)
}

object Spacing {
    val Xs = 4.dp
    val Sm = 8.dp
    val Md = 12.dp
    val Lg = 16.dp
    val Xl = 24.dp
    val Xxl = 32.dp
}

object Radius {
    val Sm = 8.dp
    val Md = 12.dp
    val Card = 16.dp
    val Button = 18.dp
    val Lg = 20.dp
    val Xl = 24.dp
    val Dialog = 28.dp
    val NavBar = 32.dp
    val Pill = 999.dp
}

val BottomNavContentPadding = 120.dp
