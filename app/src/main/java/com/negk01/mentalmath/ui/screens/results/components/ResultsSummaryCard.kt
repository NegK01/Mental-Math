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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negk01.mentalmath.ui.theme.SurfaceCard
import com.negk01.mentalmath.ui.theme.TextPrimary

@Composable
fun ResultsSummaryCard(
    correctAnswers: Int,
    averageTime: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
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
                    label = "Respuestas correctas",
                    value = correctAnswers.toString(),
                    isSuccess = true
                )

                ResultStatCard(
                    modifier = Modifier.weight(1f),
                    label = "Tiempo promedio",
                    value = averageTime,
                    isSuccess = false
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "¡Buen trabajo! 🔥",
                fontSize = 18.sp,
                color = TextPrimary
            )

            Text(
                text = "Sigue mejorando 💪",
                fontSize = 18.sp,
                color = TextPrimary
            )
        }
    }
}