package com.negk01.mentalmath.ui.screens.results.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negk01.mentalmath.R

@Composable
fun ResultsSummaryCard(
    correctAnswers: Int,
    totalRounds: Int,
    averageTime: Double
) {
    val accuracy = if (totalRounds == 0) 0.0 else correctAnswers.toDouble() / totalRounds
    val titleRes = when {
        accuracy >= 0.9 -> R.string.results_feedback_title_great
        accuracy >= 0.6 -> R.string.results_feedback_title_good
        else -> R.string.results_feedback_title_keep
    }
    val subtitleRes = when {
        accuracy >= 0.9 -> R.string.results_feedback_subtitle_great
        accuracy >= 0.6 -> R.string.results_feedback_subtitle_good
        else -> R.string.results_feedback_subtitle_keep
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ResultStatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.results_correct_answers),
                    value = correctAnswers.toString(),
                    isSuccess = true
                )

                ResultStatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.results_average_time),
                    value = stringResource(R.string.common_seconds_decimal, averageTime),
                    isSuccess = false
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = stringResource(titleRes),
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = stringResource(subtitleRes),
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
