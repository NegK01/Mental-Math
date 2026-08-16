package com.negk01.mentalmath.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.negk01.mentalmath.R
import com.negk01.mentalmath.ui.theme.Opacity
import com.negk01.mentalmath.ui.theme.Radius
import com.negk01.mentalmath.ui.theme.Spacing
import com.negk01.mentalmath.domain.model.GameRecord
import com.negk01.mentalmath.ui.utils.isDarkTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GameRecordItem(
    record: GameRecord
) {
    val formattedDate = remember(record.playedAt) {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(record.playedAt))
    }

    val averageSeconds = record.averageResponseTimeSeconds
    val itemAlpha = if (isDarkTheme()) Opacity.Half else 0.8f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Radius.Card))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = itemAlpha))
            .padding(horizontal = 14.dp, vertical = Spacing.Md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DifficultyBadge(difficulty = record.difficulty, compact = true)

        Spacer(modifier = Modifier.size(Spacing.Md))

        Text(
            text = formattedDate,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = stringResource(R.string.home_recent_score, record.correctAnswers, record.totalRounds),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.size(6.dp))

        Text(
            text = stringResource(R.string.home_recent_avg_time, averageSeconds),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
