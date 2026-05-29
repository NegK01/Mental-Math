package com.negk01.mentalmath.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.ui.utils.badgeColors
import com.negk01.mentalmath.ui.utils.toLabelResId

@Composable
fun DifficultyBadge(
    difficulty: Difficulty,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val colors = difficulty.badgeColors()
    val shape = if (compact) CircleShape else RoundedCornerShape(8.dp)
    val horizontalPadding = if (compact) 12.dp else 10.dp
    val verticalPadding = if (compact) 6.dp else 4.dp

    Box(
        modifier = modifier
            .clip(shape)
            .background(colors.container)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(difficulty.toLabelResId()),
            style = MaterialTheme.typography.labelMedium,
            color = colors.content
        )
    }
}
