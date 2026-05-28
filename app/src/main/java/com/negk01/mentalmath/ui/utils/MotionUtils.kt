package com.negk01.mentalmath.ui.utils

import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun motionEnabled(): Boolean {
    val context = LocalContext.current
    val scale = Settings.Global.getFloat(
        context.contentResolver,
        Settings.Global.ANIMATOR_DURATION_SCALE,
        1f
    )
    return scale > 0f
}
