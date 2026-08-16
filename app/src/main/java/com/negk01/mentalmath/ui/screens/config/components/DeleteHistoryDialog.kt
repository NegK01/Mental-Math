package com.negk01.mentalmath.ui.screens.config.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.negk01.mentalmath.R
import com.negk01.mentalmath.ui.components.AppDialog
import com.negk01.mentalmath.ui.components.AppDialogIcon
import com.negk01.mentalmath.ui.theme.Radius
import com.negk01.mentalmath.ui.theme.Spacing
import com.negk01.mentalmath.ui.utils.dangerButtonColors

@Composable
fun DeleteHistoryDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AppDialog(onDismiss = onDismiss, dismissOnScrimTap = true) {
        AppDialogIcon(
            imageVector = Icons.Outlined.DeleteSweep,
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(Spacing.Xs))

        Text(
            text = stringResource(R.string.config_delete_dialog_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = stringResource(R.string.config_delete_dialog_message),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Spacing.Sm))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Sm)
        ) {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.config_delete_dialog_cancel))
            }
            Button(
                onClick = onConfirm,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(Radius.Xl),
                colors = dangerButtonColors()
            ) {
                Text(stringResource(R.string.config_delete_dialog_confirm))
            }
        }
    }
}
