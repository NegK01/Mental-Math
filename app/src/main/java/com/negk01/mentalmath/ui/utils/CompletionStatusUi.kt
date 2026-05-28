package com.negk01.mentalmath.ui.utils

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.negk01.mentalmath.R
import com.negk01.mentalmath.ui.theme.AbandonedContainer
import com.negk01.mentalmath.ui.theme.Danger
import com.negk01.mentalmath.ui.theme.OnSurfaceVariantLight
import com.negk01.mentalmath.ui.theme.SuccessContainer
import com.negk01.mentalmath.ui.theme.SuccessText
import com.negk01.mentalmath.ui.theme.SurfaceVariantLight

@StringRes
fun completionStatusToDisplayNameRes(status: String): Int {
    return when (status.lowercase()) {
        "completed" -> R.string.completion_completed
        "abandoned" -> R.string.completion_abandoned
        else -> R.string.completion_unknown
    }
}

fun completionStatusToBadgeColor(status: String): Color {
    return when (status.lowercase()) {
        "completed" -> SuccessContainer
        "abandoned" -> AbandonedContainer
        else -> SurfaceVariantLight
    }
}

fun completionStatusToTextColor(status: String): Color {
    return when (status.lowercase()) {
        "completed" -> SuccessText
        "abandoned" -> Danger
        else -> OnSurfaceVariantLight
    }
}
