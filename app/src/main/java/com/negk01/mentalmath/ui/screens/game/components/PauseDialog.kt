package com.negk01.mentalmath.ui.screens.game.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PauseDialog(
    onResume: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onResume,
        title = {
            Text("Juego pausado")
        },
        text = {
            Text("Toca cualquier parte para continuar.")
        },
        confirmButton = {}
    )
}