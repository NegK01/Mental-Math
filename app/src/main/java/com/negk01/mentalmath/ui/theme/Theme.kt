package com.negk01.mentalmath.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.negk01.mentalmath.domain.model.ThemePreference

private val LightColors = lightColorScheme(
    primary = BrandPrimary,
    onPrimary = Color.White,
    primaryContainer = BrandPrimaryLight,
    onPrimaryContainer = BrandPrimary,
    secondary = BrandSecondary,
    onSecondary = Color.White,
    secondaryContainer = BrandSecondaryContainer,
    onSecondaryContainer = Color(0xFF7A5200),
    tertiary = BrandTertiary,
    onTertiary = Color.White,
    tertiaryContainer = BrandTertiaryContainer,
    onTertiaryContainer = SuccessText,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    error = Danger,
    onError = Color.White,
    errorContainer = DangerContainer,
    onErrorContainer = Color(0xFF9D2F2F),
    outline = Color(0xFFD4DBE8)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF5B37BF),
    onPrimary = Color(0xFFF4F7FB),
    primaryContainer = Color(0xFF262F4F),
    onPrimaryContainer = Color(0xFFDCE0FF),
    secondary = Color(0xFFFFC766),
    onSecondary = Color(0xFF694600),
    secondaryContainer = Color(0xFF4B370A),
    onSecondaryContainer = Color(0xFFFFE4A9),
    tertiary = Color(0xFF69E1BB),
    onTertiary = Color(0xFF00533D),
    tertiaryContainer = Color(0xFF093F32),
    onTertiaryContainer = Color(0xFFB8F5E3),
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    error = Color(0xFFFF8A80),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF7E1E23),
    onErrorContainer = Color(0xFFFFDAD6),
    outline = Color(0xFF31425C)
)

@Composable
fun MentalMathTheme(
    themePreference: ThemePreference = ThemePreference.SYSTEM,
    content: @Composable () -> Unit
) {
    val useDarkTheme = when (themePreference) {
        ThemePreference.SYSTEM -> isSystemInDarkTheme()
        ThemePreference.LIGHT -> false
        ThemePreference.DARK -> true
    }
    val colorScheme = if (useDarkTheme) DarkColors else LightColors
    val view = LocalView.current
    val activity = view.context.findActivity()

    if (!view.isInEditMode && activity != null) {
        SideEffect {
            val window = activity.window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }

            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !useDarkTheme
            insetsController.isAppearanceLightNavigationBars = !useDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

private fun Context.findActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}
