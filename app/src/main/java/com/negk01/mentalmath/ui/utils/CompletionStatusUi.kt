package com.negk01.mentalmath.ui.utils

import androidx.annotation.StringRes
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.CompletionStatus

@StringRes
fun completionStatusToDisplayNameRes(status: CompletionStatus): Int = when (status) {
    CompletionStatus.COMPLETED -> R.string.completion_completed
    CompletionStatus.ABANDONED -> R.string.completion_abandoned
}
