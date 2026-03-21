package com.negk01.mentalmath.ui.screens.config.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DangerZone(
    onDelete: () -> Unit
) {
    Button(
        onClick = onDelete,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFEF4444)
        ),
        contentPadding = PaddingValues()
    ) {
        Text("Borrar historial de puntuaciones", color = Color.White)
    }
}