package com.negk01.mentalmath.ui.screens.results.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.negk01.mentalmath.R

@Composable
fun ResultsSummarySection(
    correctAnswers: Int,
    averageTime: Double,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
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
}
