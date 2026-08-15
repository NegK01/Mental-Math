package com.negk01.mentalmath.ui.screens.game.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import com.negk01.mentalmath.ui.theme.Radius
import com.negk01.mentalmath.ui.theme.Spacing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.negk01.mentalmath.R
import com.negk01.mentalmath.ui.components.AppDialog
import com.negk01.mentalmath.ui.components.AppDialogIcon

@Composable
fun PauseDialog(onResume: () -> Unit) {
    AppDialog(onDismiss = onResume) {
        AppDialogIcon(
            imageVector = Icons.Default.Pause,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(Spacing.Xs))

        Text(
            text = stringResource(R.string.game_pause_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = stringResource(R.string.game_pause_message),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Spacing.Sm))

        Button(
            onClick = onResume,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(Radius.Xl)
        ) {
            Text(stringResource(R.string.game_resume))
        }
    }
}
