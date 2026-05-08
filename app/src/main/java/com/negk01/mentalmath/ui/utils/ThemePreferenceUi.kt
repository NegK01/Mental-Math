package com.negk01.mentalmath.ui.utils

import androidx.annotation.StringRes
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.ThemePreference

fun ThemePreference.toStorageKey(): String {
    return when (this) {
        ThemePreference.SYSTEM -> "system"
        ThemePreference.LIGHT -> "light"
        ThemePreference.ORIGINAL -> "original"
        ThemePreference.MENTA_PROFUNDO -> "menta_profundo"
        ThemePreference.OLED_RELAX -> "oled_relax"
        ThemePreference.NORDIC_FROST -> "nordic_frost"
        ThemePreference.ROYAL_DARK -> "royal_dark"
        ThemePreference.GRAPHITE_LIME -> "graphite_lime"
    }
}

fun String.toThemePreference(): ThemePreference {
    return when (lowercase()) {
        "light" -> ThemePreference.LIGHT
        "dark", "original" -> ThemePreference.ORIGINAL
        "menta_profundo" -> ThemePreference.MENTA_PROFUNDO
        "oled_relax" -> ThemePreference.OLED_RELAX
        "nordic_frost" -> ThemePreference.NORDIC_FROST
        "royal_dark" -> ThemePreference.ROYAL_DARK
        "graphite_lime" -> ThemePreference.GRAPHITE_LIME
        else -> ThemePreference.SYSTEM
    }
}

@StringRes
fun ThemePreference.toLabelResId(): Int {
    return when (this) {
        ThemePreference.SYSTEM -> R.string.theme_system
        ThemePreference.LIGHT -> R.string.theme_light
        ThemePreference.ORIGINAL -> R.string.theme_original
        ThemePreference.MENTA_PROFUNDO -> R.string.theme_menta_profundo
        ThemePreference.OLED_RELAX -> R.string.theme_oled_relax
        ThemePreference.NORDIC_FROST -> R.string.theme_nordic_frost
        ThemePreference.ROYAL_DARK -> R.string.theme_royal_dark
        ThemePreference.GRAPHITE_LIME -> R.string.theme_graphite_lime
    }
}
