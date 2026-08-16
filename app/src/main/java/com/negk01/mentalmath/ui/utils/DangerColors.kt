package com.negk01.mentalmath.ui.utils

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * Shared button colors for destructive actions.
 * Dark theme uses errorContainer/onErrorContainer for reduced visual weight;
 * light theme uses error/onError for maximum clarity.
 */
@Composable
fun dangerButtonColors(): ButtonColors {
    val isDark = isDarkTheme()
    return ButtonDefaults.buttonColors(
        containerColor = if (isDark) MaterialTheme.colorScheme.errorContainer
                         else MaterialTheme.colorScheme.error,
        contentColor = if (isDark) MaterialTheme.colorScheme.onErrorContainer
                       else MaterialTheme.colorScheme.onError
    )
}
