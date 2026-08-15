package com.negk01.mentalmath.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.ui.unit.dp

object Opacity {
    const val Focus = 0.12f
    const val BadgeContainer = 0.18f
    const val Scrim = 0.6f
    const val Half = 0.5f
}

object Motion {
    const val Fast = 150
    const val Medium = 250
    const val Slow = 400
    const val Celebration = 600
    const val ExtraSlow = 800
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
}

val BottomNavContentPadding = 120.dp
