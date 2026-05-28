package com.negk01.mentalmath.ui.utils

import android.content.Context
import android.view.accessibility.AccessibilityManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun motionEnabled(): Boolean {
    val context = LocalContext.current
    val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as? AccessibilityManager
    return am?.areAnimatorsEnabled() ?: true
}
