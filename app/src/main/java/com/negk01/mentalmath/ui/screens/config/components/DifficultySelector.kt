package com.negk01.mentalmath.ui.screens.config.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.ui.utils.badgeColors
import com.negk01.mentalmath.ui.utils.toLabelResId

@Composable
fun DifficultySelector(
    selected: Difficulty,
    onSelect: (Difficulty) -> Unit
) {
    val isDarkTheme = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    val options = listOf(
        Difficulty.EASY,
        Difficulty.MEDIUM,
        Difficulty.HARD
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.config_difficulty_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            options.forEachIndexed { index, difficulty ->
                val badgeColors = difficulty.badgeColors(isDarkTheme = isDarkTheme)
                DifficultyItem(
                    text = stringResource(difficulty.toLabelResId()),
                    selected = selected == difficulty,
                    accentColor = badgeColors.container,
                    accentTextColor = badgeColors.content,
                    onClick = { onSelect(difficulty) }
                )

                if (index != options.lastIndex) {
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        }
    }
}
