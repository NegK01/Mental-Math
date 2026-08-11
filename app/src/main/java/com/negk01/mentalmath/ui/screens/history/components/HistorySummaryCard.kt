package com.negk01.mentalmath.ui.screens.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.domain.model.GameRecord
import com.negk01.mentalmath.ui.components.DifficultyBadge
import com.negk01.mentalmath.ui.theme.Radius

@Composable
fun HistorySummaryCard(
    totalGames: Int,
    averageAccuracy: Double,
    averageTime: Double,
    bestEasy: GameRecord? = null,
    bestMedium: GameRecord? = null,
    bestHard: GameRecord? = null,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = stringResource(R.string.history_summary_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = stringResource(R.string.history_summary_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            MetricCard(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.history_total_games),
                value = totalGames.toString(),
                valueColor = MaterialTheme.colorScheme.onSurface
            )
            MetricCard(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.history_average_accuracy),
                value = stringResource(R.string.history_accuracy_value, averageAccuracy),
                valueColor = MaterialTheme.colorScheme.onSurface
            )
            MetricCard(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.history_average_time),
                value = stringResource(R.string.history_time_value, averageTime),
                valueColor = MaterialTheme.colorScheme.onSurface
            )
        }

        PersonalBestsCard(
            bestEasy = bestEasy,
            bestMedium = bestMedium,
            bestHard = bestHard
        )
    }
}

@Composable
private fun PersonalBestsCard(
    bestEasy: GameRecord?,
    bestMedium: GameRecord?,
    bestHard: GameRecord?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Radius.Card))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.50f))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.history_best_records_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            listOf(
                Difficulty.EASY to bestEasy,
                Difficulty.MEDIUM to bestMedium,
                Difficulty.HARD to bestHard
            ).forEachIndexed { index, (difficulty, record) ->
                if (record != null) {
                    if (index > 0) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f),
                            thickness = 1.dp
                        )
                    }
                    BestRecordRow(difficulty = difficulty, record = record)
                }
            }
        }
    }
}

@Composable
private fun BestRecordRow(difficulty: Difficulty, record: GameRecord) {
    val averageSeconds = record.averageResponseTimeMillis / 1000.0
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DifficultyBadge(difficulty = difficulty, compact = true)
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.home_recent_score, record.correctAnswers, record.totalRounds),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = stringResource(R.string.home_recent_avg_time, averageSeconds),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier,
    label: String,
    value: String,
    valueColor: Color
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(Radius.Button))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.72f))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
        }
    }
}
