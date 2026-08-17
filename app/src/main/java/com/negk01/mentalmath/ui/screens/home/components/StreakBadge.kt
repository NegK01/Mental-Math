package com.negk01.mentalmath.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.negk01.mentalmath.R
import com.negk01.mentalmath.ui.theme.Opacity
import com.negk01.mentalmath.ui.theme.Spacing
import com.negk01.mentalmath.ui.theme.Warning

@Composable
fun StreakBadge(
    dailyStreak: Int,
    onClick: () -> Unit
) {
    val streakColor = Warning
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(streakColor.copy(alpha = Opacity.BadgeContainer))
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.Md, vertical = Spacing.Sm),
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = stringResource(R.string.home_daily_streak_title),
                tint = streakColor,
                modifier = Modifier.size(Spacing.Xl)
            )

            Spacer(modifier = Modifier.size(Spacing.Xs))

            Text(
                text = stringResource(R.string.home_daily_streak_value, dailyStreak),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
