package com.negk01.mentalmath.ui.screens.config.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.negk01.mentalmath.ui.utils.dangerButtonColors

@Composable
fun DangerZone(
    buttonText: String,
    onDelete: () -> Unit
) {
    Button(
        onClick = onDelete,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = dangerButtonColors(),
        contentPadding = PaddingValues()
    ) {
        Text(buttonText)
    }
}
