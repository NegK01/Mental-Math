package com.negk01.mentalmath.data.mapper

import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.domain.model.LanguagePreference
import com.negk01.mentalmath.domain.model.ThemePreference

fun Difficulty.toStorageKey(): String = when (this) {
    Difficulty.EASY -> "easy"
    Difficulty.MEDIUM -> "medium"
    Difficulty.HARD -> "hard"
}

fun String.toDifficulty(): Difficulty = when (lowercase()) {
    "easy" -> Difficulty.EASY
    "hard" -> Difficulty.HARD
    else -> Difficulty.MEDIUM
}

fun ThemePreference.toStorageKey(): String = when (this) {
    ThemePreference.SYSTEM -> "system"
    ThemePreference.LIGHT -> "light"
    ThemePreference.ORIGINAL -> "original"
    ThemePreference.MENTA_PROFUNDO -> "menta_profundo"
    ThemePreference.OLED_RELAX -> "oled_relax"
    ThemePreference.NORDIC_FROST -> "nordic_frost"
    ThemePreference.ROYAL_DARK -> "royal_dark"
    ThemePreference.GRAPHITE_LIME -> "graphite_lime"
}

fun String.toThemePreference(): ThemePreference = when (lowercase()) {
    "light" -> ThemePreference.LIGHT
    "dark", "original" -> ThemePreference.ORIGINAL
    "menta_profundo" -> ThemePreference.MENTA_PROFUNDO
    "oled_relax" -> ThemePreference.OLED_RELAX
    "nordic_frost" -> ThemePreference.NORDIC_FROST
    "royal_dark" -> ThemePreference.ROYAL_DARK
    "graphite_lime" -> ThemePreference.GRAPHITE_LIME
    else -> ThemePreference.SYSTEM
}

fun LanguagePreference.toStorageKey(): String = when (this) {
    LanguagePreference.SYSTEM -> "system"
    LanguagePreference.SPANISH -> "es"
    LanguagePreference.ENGLISH -> "en"
}

fun String.toLanguagePreference(): LanguagePreference = when (lowercase()) {
    "es" -> LanguagePreference.SPANISH
    "en" -> LanguagePreference.ENGLISH
    else -> LanguagePreference.SYSTEM
}
