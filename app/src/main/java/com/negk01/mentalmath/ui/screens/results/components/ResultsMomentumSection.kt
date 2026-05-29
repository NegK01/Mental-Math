package com.negk01.mentalmath.ui.screens.results.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.negk01.mentalmath.R
import com.negk01.mentalmath.ui.theme.Radius
import com.negk01.mentalmath.presentation.results.DeltaState

@Composable
fun ResultsMomentumSection(
    maxStreak: Int,
    delta: DeltaState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
    ) {
        MomentumChip(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.LocalFireDepartment,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(16.dp)
                )
            },
            label = stringResource(R.string.results_max_streak),
            value = maxStreak.toString(),
            accentColor = MaterialTheme.colorScheme.secondary
        )

        when (delta) {
            is DeltaState.NewRecord -> MomentumChip(
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.EmojiEvents,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(16.dp)
                    )
                },
                label = stringResource(R.string.results_new_record),
                value = null,
                accentColor = MaterialTheme.colorScheme.tertiary
            )

            is DeltaState.FirstGame -> MomentumChip(
                icon = null,
                label = stringResource(R.string.results_first_game),
                value = null,
                accentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )

            is DeltaState.None -> Unit
        }
    }
}

@Composable
private fun MomentumChip(
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)?,
    label: String,
    value: String?,
    accentColor: androidx.compose.ui.graphics.Color
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(Radius.Md))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.50f))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            icon?.invoke()
            Text(
                text = if (value != null) "$label $value" else label,
                style = MaterialTheme.typography.labelMedium,
                color = accentColor
            )
        }
    }
}
