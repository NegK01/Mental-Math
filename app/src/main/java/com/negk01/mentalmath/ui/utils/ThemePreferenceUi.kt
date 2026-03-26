package com.negk01.mentalmath.ui.utils

import androidx.annotation.StringRes
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.ThemePreference

fun ThemePreference.toStorageKey(): String {
    return when (this) {
        ThemePreference.SYSTEM -> "system"
        ThemePreference.LIGHT -> "light"
        ThemePreference.DARK -> "dark"
    }
}

fun String.toThemePreference(): ThemePreference {
    return when (lowercase()) {
        "light" -> ThemePreference.LIGHT
        "dark" -> ThemePreference.DARK
        else -> ThemePreference.SYSTEM
    }
}

@StringRes
fun ThemePreference.toLabelResId(): Int {
    return when (this) {
        ThemePreference.SYSTEM -> R.string.theme_system
        ThemePreference.LIGHT -> R.string.theme_light
        ThemePreference.DARK -> R.string.theme_dark
    }
}
