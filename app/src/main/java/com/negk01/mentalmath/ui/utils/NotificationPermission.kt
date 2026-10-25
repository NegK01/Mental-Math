package com.negk01.mentalmath.ui.utils

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.negk01.mentalmath.data.notification.NotificationHelper
import kotlinx.coroutines.launch

data class NotificationPermissionState(
    val isEnabled: Boolean,
    val request: () -> Unit
)

@Composable
fun rememberNotificationPermissionState(
    hasRequestedPermission: Boolean,
    onPermissionRequested: suspend () -> Boolean,
    onGranted: () -> Unit
): NotificationPermissionState {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val permissionScope = rememberCoroutineScope()
    val currentOnGranted by rememberUpdatedState(onGranted)
    var isEnabled by remember(context) {
        mutableStateOf(NotificationHelper.areNotificationsEnabled(context))
    }
    var waitingForSettings by rememberSaveable { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        isEnabled = NotificationHelper.areNotificationsEnabled(context)
        if (isEnabled) currentOnGranted()
    }

    DisposableEffect(lifecycleOwner, context) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isEnabled = NotificationHelper.areNotificationsEnabled(context)
                if (waitingForSettings) {
                    waitingForSettings = false
                    if (isEnabled) currentOnGranted()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    return NotificationPermissionState(isEnabled) {
        when {
            NotificationHelper.areNotificationsEnabled(context) -> currentOnGranted()
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED &&
                (!hasRequestedPermission || context.findActivity()
                    ?.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) == true) -> {
                permissionScope.launch {
                    if (onPermissionRequested()) {
                        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }
            else -> {
                waitingForSettings = true
                context.startActivity(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                })
            }
        }
    }
}
