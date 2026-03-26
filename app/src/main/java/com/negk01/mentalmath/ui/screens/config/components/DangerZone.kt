package com.negk01.mentalmath.ui.screens.config.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp

@Composable
fun DangerZone(
    buttonText: String,
    onDelete: () -> Unit
) {
    val isDarkTheme = MaterialTheme.colorScheme.surface.luminance() < 0.5f

    Button(
        onClick = onDelete,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isDarkTheme) {
                MaterialTheme.colorScheme.errorContainer
            } else {
                MaterialTheme.colorScheme.error
            },
            contentColor = if (isDarkTheme) {
                MaterialTheme.colorScheme.onErrorContainer
            } else {
                MaterialTheme.colorScheme.onError
            }
        ),
        contentPadding = PaddingValues()
    ) {
        Text(buttonText)
    }
}
