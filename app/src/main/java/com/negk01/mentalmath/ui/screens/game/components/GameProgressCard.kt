package com.negk01.mentalmath.ui.screens.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negk01.mentalmath.ui.theme.Primary
import com.negk01.mentalmath.ui.theme.SurfaceCard
import com.negk01.mentalmath.ui.theme.TextPrimary
import com.negk01.mentalmath.ui.theme.TextSecondary

@Composable
fun GameProgressCard(
    difficulty: String,
    currentRound: Int,
    totalRounds: Int,
    timeLeftSeconds: Int
) {
    val progress = currentRound.toFloat() / totalRounds.toFloat()

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
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = difficulty,
                    fontSize = 16.sp,
                    color = TextPrimary
                )

                Text(
                    text = "Tiempo: ${timeLeftSeconds}s",
                    fontSize = 16.sp,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = Primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ronda $currentRound de $totalRounds",
                fontSize = 14.sp,
                color = TextSecondary
            )
        }
    }
}