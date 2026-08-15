package com.negk01.mentalmath.ui.screens.results.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.negk01.mentalmath.R
import com.negk01.mentalmath.ui.screens.results.RoundDetail

@Composable
fun RoundDetailsSection(
    items: List<RoundDetail>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.results_round_details_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (items.isEmpty()) {
            Text(
                text = stringResource(R.string.results_round_details_empty),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            items.forEachIndexed { index, item ->
                RoundDetailItem(item = item)
                if (index < items.lastIndex) {
                    Spacer(modifier = Modifier.size(6.dp))
                }
            }
        }
    }
}
