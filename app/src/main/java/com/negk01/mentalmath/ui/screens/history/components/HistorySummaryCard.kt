package com.negk01.mentalmath.ui.screens.history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negk01.mentalmath.ui.theme.SurfaceCard
import com.negk01.mentalmath.ui.theme.TextPrimary

@Composable
fun HistorySummaryCard(
    totalGames: Int,
    averageAccuracy: Double,
    averageTime: Double,
    //bestStreak: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = "Resumen",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Text("Partidas: $totalGames")
            Text("Precisión: ${String.format("%.0f%%", averageAccuracy)}")
            Text("Tiempo promedio: ${String.format("%.1fs", averageTime)}")
            //Text("Mejor racha: $bestStreak")
        }
    }
}