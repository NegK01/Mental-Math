package com.negk01.mentalmath.ui.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negk01.mentalmath.domain.model.GameRecord
import com.negk01.mentalmath.ui.theme.SurfaceCard
import com.negk01.mentalmath.ui.theme.TextPrimary
import com.negk01.mentalmath.ui.theme.Warning
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun RecentScoresCard(
    records: List<GameRecord>
) {
    Card(
        modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = androidx.compose.ui.Modifier.padding(16.dp)
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Últimas 3 partidas",
                    tint = Warning,
                    modifier = androidx.compose.ui.Modifier.size(22.dp)
                )

                Spacer(
                    modifier = androidx.compose.ui.Modifier.size(8.dp)
                )

                Text(
                    text = "Últimas 3 partidas",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = androidx.compose.ui.Modifier.height(14.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (records.isEmpty()) {
                    Text(
                        text = "Aún no hay partidas guardadas.",
                        fontSize = 14.sp,
                        color = Color.Gray
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