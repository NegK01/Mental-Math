package com.negk01.mentalmath.ui.screens.results.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
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
import com.negk01.mentalmath.ui.theme.Spacing
import com.negk01.mentalmath.domain.model.RoundDetail
import com.negk01.mentalmath.ui.utils.isDarkTheme

@Composable
fun RoundDetailItem(
    item: RoundDetail
) {
    val isDark = isDarkTheme()
    val itemBg = if (item.isCorrect) {
        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = if (isDark) 0.25f else 0.6f)
    } else {
        MaterialTheme.colorScheme.errorContainer.copy(alpha = if (isDark) 0.25f else 0.55f)
    }
    val iconTint = if (item.isCorrect) {
        MaterialTheme.colorScheme.tertiary
    } else {
        MaterialTheme.colorScheme.error
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Radius.Md))
            .background(itemBg)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(Spacing.Xs)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.expression,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.size(Spacing.Sm))

            Icon(
                imageVector = if (item.isCorrect) Icons.Rounded.Check else Icons.Rounded.Close,
                contentDescription = null,
                tint = iconTint
            )

            Spacer(modifier = Modifier.size(6.dp))

            Text(
                text = item.time,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (!item.isCorrect) {
            Text(
                text = stringResource(R.string.results_wrong_answer_label, item.userAnswer),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
