package com.negk01.mentalmath.ui.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.GameRecord
import com.negk01.mentalmath.ui.theme.RecentAccent
import com.negk01.mentalmath.ui.theme.RecentAccentContainer

@Composable
fun RecentScoresCard(
    records: List<GameRecord>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = RecentAccentContainer)
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = stringResource(R.string.home_recent_scores_title),
                        tint = RecentAccent,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.size(10.dp))

                Text(
                    text = stringResource(R.string.home_recent_scores_title),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (records.isEmpty()) {
                    Text(
                        text = stringResource(R.string.history_empty),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    records.forEach { record ->
                        RecentScoreItem(record = record)
                    }
                }
            }
        }
    }
}
