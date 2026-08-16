package com.negk01.mentalmath.ui.screens.results.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.game.OperatorInsight
import com.negk01.mentalmath.ui.theme.Spacing
import com.negk01.mentalmath.ui.utils.toLabelResId

@Composable
fun OperatorInsightRow(
    insight: OperatorInsight,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Lightbulb,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(Spacing.Lg)
        )
        Spacer(modifier = Modifier.width(Spacing.Sm))
        Text(
            text = stringResource(
                R.string.results_operator_insight,
                stringResource(insight.operator.toLabelResId()),
                insight.avgSeconds
            ),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
