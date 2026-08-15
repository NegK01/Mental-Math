package com.negk01.mentalmath.ui.screens.config.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.LanguagePreference
import com.negk01.mentalmath.ui.components.OptionSelector
import com.negk01.mentalmath.ui.utils.toLabelResId

@Composable
fun LanguagePreferenceSelector(
    selected: LanguagePreference,
    onSelect: (LanguagePreference) -> Unit
) {
    OptionSelector(
        title = stringResource(R.string.config_language_title),
        options = listOf(
            LanguagePreference.SYSTEM,
            LanguagePreference.ENGLISH,
            LanguagePreference.SPANISH
        ),
        selected = selected,
        onSelect = onSelect,
        optionLabelRes = { it.toLabelResId() }
    )
}
