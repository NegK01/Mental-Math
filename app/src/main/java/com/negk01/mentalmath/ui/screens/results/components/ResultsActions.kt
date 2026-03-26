package com.negk01.mentalmath.ui.screens.results.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.negk01.mentalmath.R

@Composable
fun ResultsActions(
    onGoHome: () -> Unit,
    onPlayAgain: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onGoHome,
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
        ) {
            Text(stringResource(R.string.results_go_home))
        }

        Button(
            onClick = onPlayAgain,
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
        ) {
            Text(stringResource(R.string.results_play_again))
        }
    }
}
