package com.negk01.mentalmath.ui.screens.game.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.negk01.mentalmath.R

@Composable
fun PauseDialog(
    onResume: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onResume,
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        title = {
            Text(stringResource(R.string.game_pause_title))
        },
        text = {
            Text(stringResource(R.string.game_pause_message))
        },
        confirmButton = {
            TextButton(onClick = onResume) {
                Text(stringResource(R.string.game_resume))
            }
        }
    )
}
