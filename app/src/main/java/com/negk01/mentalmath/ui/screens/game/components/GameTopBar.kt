package com.negk01.mentalmath.ui.screens.game.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.negk01.mentalmath.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopBar(
    onPauseClick: () -> Unit,
    onExitClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.96f),
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
        ),
        title = {
            Text(text = stringResource(R.string.app_name))
        },
        navigationIcon = {
            IconButton(onClick = onExitClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.game_exit)
                )
            }
        },
        actions = {
            IconButton(onClick = onPauseClick) {
                Icon(
                    imageVector = Icons.Default.Pause,
                    contentDescription = stringResource(R.string.game_pause)
                )
            }
        }
    )
}
