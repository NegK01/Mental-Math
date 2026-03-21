package com.negk01.mentalmath.ui.screens.config.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DifficultySelector(
    selected: String,
    onSelect: (String) -> Unit
) {

    val options = listOf("Fácil", "Medio", "Difícil")

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text("Nivel de dificultad")

            Spacer(modifier = Modifier.height(12.dp))

            options.forEach { option ->
                DifficultyItem(
                    text = option,
                    selected = selected == option,
                    onClick = { onSelect(option) }
                )
            }
        }
    }
}