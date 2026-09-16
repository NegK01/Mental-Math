package com.negk01.mentalmath.ui.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.core.os.LocaleListCompat
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.LanguagePreference
import androidx.compose.ui.platform.LocalLocale

fun LanguagePreference.toLocaleListCompat(): LocaleListCompat {
    return when (this) {
        LanguagePreference.SYSTEM -> LocaleListCompat.getEmptyLocaleList()
        LanguagePreference.ENGLISH -> LocaleListCompat.forLanguageTags("en")
        LanguagePreference.SPANISH -> LocaleListCompat.forLanguageTags("es")
        LanguagePreference.PORTUGUESE -> LocaleListCompat.forLanguageTags("pt")
    }
}

@StringRes
fun LanguagePreference.toLabelResId(): Int {
    return when (this) {
        LanguagePreference.SYSTEM -> R.string.language_system
        LanguagePreference.ENGLISH -> R.string.language_english
        LanguagePreference.SPANISH -> R.string.language_spanish
        LanguagePreference.PORTUGUESE -> R.string.language_portuguese
    }
}

@Composable
fun resolveDisplayLanguage(preference: LanguagePreference): LanguagePreference {
    if (preference != LanguagePreference.SYSTEM) return preference
    val languageCode  = LocalLocale.current.language.lowercase()
    return when (languageCode) {
        "es" -> LanguagePreference.SPANISH
        "pt" -> LanguagePreference.PORTUGUESE
        else -> LanguagePreference.ENGLISH
    }
}