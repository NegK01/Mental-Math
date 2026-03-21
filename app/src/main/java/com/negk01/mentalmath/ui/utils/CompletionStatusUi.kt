package com.negk01.mentalmath.ui.utils

import androidx.compose.ui.graphics.Color
import com.negk01.mentalmath.ui.theme.Danger
import com.negk01.mentalmath.ui.theme.EasyBadge
import com.negk01.mentalmath.ui.theme.EasyText

fun completionStatusToDisplayName(status: String): String {
    return when (status.lowercase()) {
        "completed" -> "Completada"
        "abandoned" -> "Abandonada"
        else -> "Desconocido"
    }
}

fun completionStatusToBadgeColor(status: String): Color {
    return when (status.lowercase()) {
        "completed" -> EasyBadge
        "abandoned" -> Color(0xFFF8D7DA)
        else -> Color.LightGray
    }
}

fun completionStatusToTextColor(status: String): Color {
    return when (status.lowercase()) {
        "completed" -> EasyText
        "abandoned" -> Danger
        else -> Color.DarkGray
    }
}