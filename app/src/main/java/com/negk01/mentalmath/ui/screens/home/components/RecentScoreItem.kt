package com.negk01.mentalmath.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.GameRecord
import com.negk01.mentalmath.ui.utils.badgeColors
import com.negk01.mentalmath.ui.utils.toLabelResId
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RecentScoreItem(
    record: GameRecord
) {
    val badgeColors = record.difficulty.badgeColors()

    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        .format(Date(record.playedAt))

    val averageSeconds = record.averageResponseTimeMillis / 1000.0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(badgeColors.container)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(record.difficulty.toLabelResId()),
                fontSize = 13.sp,
                color = badgeColors.content,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = formattedDate,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = stringResource(
                R.string.home_recent_score_summary,
                record.correctAnswers,
                record.totalRounds,
                averageSeconds
            ),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}
