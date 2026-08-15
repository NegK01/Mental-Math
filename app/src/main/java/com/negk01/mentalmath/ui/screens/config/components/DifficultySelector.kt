package com.negk01.mentalmath.ui.screens.config.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.ui.components.OptionSelector
import com.negk01.mentalmath.ui.utils.toLabelResId

@Composable
fun DifficultySelector(
    selected: Difficulty,
    onSelect: (Difficulty) -> Unit
) {
    OptionSelector(
        title = stringResource(R.string.config_difficulty_title),
        options = listOf(Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD),
        selected = selected,
        onSelect = onSelect,
        optionLabelRes = { it.toLabelResId() }
    )
}
