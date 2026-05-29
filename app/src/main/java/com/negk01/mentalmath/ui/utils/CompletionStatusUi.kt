package com.negk01.mentalmath.ui.utils

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.CompletionStatus
import com.negk01.mentalmath.ui.theme.AbandonedContainer
import com.negk01.mentalmath.ui.theme.Danger
import com.negk01.mentalmath.ui.theme.OnSurfaceVariantLight
import com.negk01.mentalmath.ui.theme.SuccessContainer
import com.negk01.mentalmath.ui.theme.SuccessText
import com.negk01.mentalmath.ui.theme.SurfaceVariantLight

@StringRes
fun completionStatusToDisplayNameRes(status: CompletionStatus): Int = when (status) {
    CompletionStatus.COMPLETED -> R.string.completion_completed
    CompletionStatus.ABANDONED -> R.string.completion_abandoned
}

fun completionStatusToBadgeColor(status: CompletionStatus): Color = when (status) {
    CompletionStatus.COMPLETED -> SuccessContainer
    CompletionStatus.ABANDONED -> AbandonedContainer
}

fun completionStatusToTextColor(status: CompletionStatus): Color = when (status) {
    CompletionStatus.COMPLETED -> SuccessText
    CompletionStatus.ABANDONED -> Danger
}
