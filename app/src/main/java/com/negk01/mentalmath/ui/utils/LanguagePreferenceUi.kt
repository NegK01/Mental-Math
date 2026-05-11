package com.negk01.mentalmath.ui.utils

import androidx.annotation.StringRes
import androidx.core.os.LocaleListCompat
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.LanguagePreference

fun LanguagePreference.toLocaleListCompat(): LocaleListCompat {
    return when (this) {
        LanguagePreference.SYSTEM -> LocaleListCompat.getEmptyLocaleList()
        LanguagePreference.SPANISH -> LocaleListCompat.forLanguageTags("es")
        LanguagePreference.ENGLISH -> LocaleListCompat.forLanguageTags("en")
    }
}

@StringRes
fun LanguagePreference.toLabelResId(): Int {
    return when (this) {
        LanguagePreference.SYSTEM -> R.string.language_system
        LanguagePreference.SPANISH -> R.string.language_spanish
        LanguagePreference.ENGLISH -> R.string.language_english
    }
}
