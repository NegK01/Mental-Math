package com.negk01.mentalmath.ui.utils

import androidx.annotation.StringRes
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.ThemePreference

@StringRes
fun ThemePreference.toLabelResId(): Int {
    return when (this) {
        ThemePreference.SYSTEM -> R.string.theme_system
        ThemePreference.LIGHT -> R.string.theme_light
        ThemePreference.ORIGINAL -> R.string.theme_original
        ThemePreference.MENTA_PROFUNDO -> R.string.theme_menta_profundo
        ThemePreference.OLED -> R.string.theme_oled
        ThemePreference.NORDIC_FROST -> R.string.theme_nordic_frost
        ThemePreference.ROYAL_DARK -> R.string.theme_royal_dark
        ThemePreference.GRAPHITE_LIME -> R.string.theme_graphite_lime
    }
}
