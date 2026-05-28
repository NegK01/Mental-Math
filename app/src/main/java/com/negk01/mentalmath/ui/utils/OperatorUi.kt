package com.negk01.mentalmath.ui.utils

import androidx.annotation.StringRes
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.Operator

@StringRes
fun Operator.toLabelResId(): Int = when (this) {
    Operator.ADD -> R.string.operator_add
    Operator.SUBTRACT -> R.string.operator_subtract
    Operator.MULTIPLY -> R.string.operator_multiply
    Operator.DIVIDE -> R.string.operator_divide
}
