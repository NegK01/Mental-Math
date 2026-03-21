package com.negk01.mentalmath.ui.screens.game.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.negk01.mentalmath.ui.theme.SurfaceCard
import com.negk01.mentalmath.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopBar(
    onPauseClick: () -> Unit,
    onExitClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Mental Math",
                color = TextPrimary
            )
        },
        navigationIcon = {
            IconButton(onClick = onExitClick) {
                Icon(Icons.Default.Close, contentDescription = "Salir")
            }
        },
        actions = {
            IconButton(onClick = onPauseClick) {
                Icon(Icons.Default.Pause, contentDescription = "Pausar")
            }
        }
    )
}