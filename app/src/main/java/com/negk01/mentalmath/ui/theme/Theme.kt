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

private val SharedDarkSecondary = Color(0xFFFBBF24)
private val SharedDarkOnSecondary = Color.Black
private val SharedDarkSecondaryContainer = Color(0xFF3D2E00)
private val SharedDarkOnSecondaryContainer = Color(0xFFFBBF24)
private val SharedDarkTertiary = Color(0xFF4ADE80)
private val SharedDarkOnTertiary = Color.Black
private val SharedDarkTertiaryContainer = Color(0xFF003322)
private val SharedDarkOnTertiaryContainer = Color(0xFF4ADE80)
private val SharedDarkError = Color(0xFFF87171)
private val SharedDarkOnError = Color.Black
private val SharedDarkErrorContainer = Color(0xFF3E0000)
private val SharedDarkOnErrorContainer = Color(0xFFF87171)

private val OriginalColors = darkColorScheme(
    primary = OriginalAccent,
    onPrimary = OriginalOnAccent,
    primaryContainer = Color(0xFF2A1D6E),
    onPrimaryContainer = Color(0xFFD4C5FF),
    secondary = SharedDarkSecondary,
    onSecondary = SharedDarkOnSecondary,
    secondaryContainer = SharedDarkSecondaryContainer,
    onSecondaryContainer = SharedDarkOnSecondaryContainer,
    tertiary = SharedDarkTertiary,
    onTertiary = SharedDarkOnTertiary,
    tertiaryContainer = SharedDarkTertiaryContainer,
    onTertiaryContainer = SharedDarkOnTertiaryContainer,
    background = OriginalBg,
    onBackground = OriginalTextPrimary,
    surface = OriginalSurface,
    onSurface = OriginalTextPrimary,
    surfaceVariant = OriginalSurfaceVariant,
    onSurfaceVariant = OriginalTextSecondary,
    error = SharedDarkError,
    onError = SharedDarkOnError,
    errorContainer = SharedDarkErrorContainer,
    onErrorContainer = SharedDarkOnErrorContainer,
    outline = OriginalOutline
)

private val MentaColors = darkColorScheme(
    primary = MentaAccent,
    onPrimary = MentaOnAccent,
    primaryContainer = Color(0xFF003D35),
    onPrimaryContainer = Color(0xFF8EFFF0),
    secondary = SharedDarkSecondary,
    onSecondary = SharedDarkOnSecondary,
    secondaryContainer = SharedDarkSecondaryContainer,
    onSecondaryContainer = SharedDarkOnSecondaryContainer,
    tertiary = SharedDarkTertiary,
    onTertiary = SharedDarkOnTertiary,
    tertiaryContainer = SharedDarkTertiaryContainer,
    onTertiaryContainer = SharedDarkOnTertiaryContainer,
    background = MentaBg,
    onBackground = MentaTextPrimary,
    surface = MentaSurface,
    onSurface = MentaTextPrimary,
    surfaceVariant = MentaSurfaceVariant,
    onSurfaceVariant = MentaTextSecondary,
    error = SharedDarkError,
    onError = SharedDarkOnError,
    errorContainer = SharedDarkErrorContainer,
    onErrorContainer = SharedDarkOnErrorContainer,
    outline = MentaOutline
)

private val OledColors = darkColorScheme(
    primary = OledAccent,
    onPrimary = OledOnAccent,
    primaryContainer = Color(0xFF1A1A6E),
    onPrimaryContainer = Color(0xFFCCCBFF),
    secondary = SharedDarkSecondary,
    onSecondary = SharedDarkOnSecondary,
    secondaryContainer = SharedDarkSecondaryContainer,
    onSecondaryContainer = SharedDarkOnSecondaryContainer,
    tertiary = SharedDarkTertiary,
    onTertiary = SharedDarkOnTertiary,
    tertiaryContainer = SharedDarkTertiaryContainer,
    onTertiaryContainer = SharedDarkOnTertiaryContainer,
    background = OledBg,
    onBackground = OledTextPrimary,
    surface = OledSurface,
    onSurface = OledTextPrimary,
    surfaceVariant = OledSurfaceVariant,
    onSurfaceVariant = OledTextSecondary,
    error = SharedDarkError,
    onError = SharedDarkOnError,
    errorContainer = SharedDarkErrorContainer,
    onErrorContainer = SharedDarkOnErrorContainer,
    outline = OledOutline
)

private val NordicColors = darkColorScheme(
    primary = NordicAccent,
    onPrimary = NordicOnAccent,
    primaryContainer = Color(0xFF0F3D42),
    onPrimaryContainer = Color(0xFFB8EFF4),
    secondary = SharedDarkSecondary,
    onSecondary = SharedDarkOnSecondary,
    secondaryContainer = SharedDarkSecondaryContainer,
    onSecondaryContainer = SharedDarkOnSecondaryContainer,
    tertiary = SharedDarkTertiary,
    onTertiary = SharedDarkOnTertiary,
    tertiaryContainer = SharedDarkTertiaryContainer,
    onTertiaryContainer = SharedDarkOnTertiaryContainer,
    background = NordicBg,
    onBackground = NordicTextPrimary,
    surface = NordicSurface,
    onSurface = NordicTextPrimary,
    surfaceVariant = NordicSurfaceVariant,
    onSurfaceVariant = NordicTextSecondary,
    error = SharedDarkError,
    onError = SharedDarkOnError,
    errorContainer = SharedDarkErrorContainer,
    onErrorContainer = SharedDarkOnErrorContainer,
    outline = NordicOutline
)

private val RoyalColors = darkColorScheme(
    primary = RoyalAccent,
    onPrimary = RoyalOnAccent,
    primaryContainer = Color(0xFF0B2E62),
    onPrimaryContainer = Color(0xFFBDD7FF),
    secondary = SharedDarkSecondary,
    onSecondary = SharedDarkOnSecondary,
    secondaryContainer = SharedDarkSecondaryContainer,
    onSecondaryContainer = SharedDarkOnSecondaryContainer,
    tertiary = SharedDarkTertiary,
    onTertiary = SharedDarkOnTertiary,
    tertiaryContainer = SharedDarkTertiaryContainer,
    onTertiaryContainer = SharedDarkOnTertiaryContainer,
    background = RoyalBg,
    onBackground = RoyalTextPrimary,
    surface = RoyalSurface,
    onSurface = RoyalTextPrimary,
    surfaceVariant = RoyalSurfaceVariant,
    onSurfaceVariant = RoyalTextSecondary,
    error = SharedDarkError,
    onError = SharedDarkOnError,
    errorContainer = SharedDarkErrorContainer,
    onErrorContainer = SharedDarkOnErrorContainer,
    outline = RoyalOutline
)

private val GraphiteColors = darkColorScheme(
    primary = GraphiteAccent,
    onPrimary = GraphiteOnAccent,
    primaryContainer = Color(0xFF1A3A0A),
    onPrimaryContainer = Color(0xFFD0FFAD),
    secondary = SharedDarkSecondary,
    onSecondary = SharedDarkOnSecondary,
    secondaryContainer = SharedDarkSecondaryContainer,
    onSecondaryContainer = SharedDarkOnSecondaryContainer,
    tertiary = SharedDarkTertiary,
    onTertiary = SharedDarkOnTertiary,
    tertiaryContainer = SharedDarkTertiaryContainer,
    onTertiaryContainer = SharedDarkOnTertiaryContainer,
    background = GraphiteBg,
    onBackground = GraphiteTextPrimary,
    surface = GraphiteSurface,
    onSurface = GraphiteTextPrimary,
    surfaceVariant = GraphiteSurfaceVariant,
    onSurfaceVariant = GraphiteTextSecondary,
    error = SharedDarkError,
    onError = SharedDarkOnError,
    errorContainer = SharedDarkErrorContainer,
    onErrorContainer = SharedDarkOnErrorContainer,
    outline = GraphiteOutline
)

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

@Composable
fun MentalMathTheme(
    themePreference: ThemePreference = ThemePreference.SYSTEM,
    content: @Composable () -> Unit
) {
    val systemIsDark = isSystemInDarkTheme()
    val isDark = when (themePreference) {
        ThemePreference.LIGHT -> false
        ThemePreference.SYSTEM -> systemIsDark
        else -> true
    }
    val colorScheme = when (themePreference) {
        ThemePreference.LIGHT -> LightColors
        ThemePreference.SYSTEM -> if (systemIsDark) OriginalColors else LightColors
        ThemePreference.ORIGINAL -> OriginalColors
        ThemePreference.MENTA_PROFUNDO -> MentaColors
        ThemePreference.OLED_RELAX -> OledColors
        ThemePreference.NORDIC_FROST -> NordicColors
        ThemePreference.ROYAL_DARK -> RoyalColors
        ThemePreference.GRAPHITE_LIME -> GraphiteColors
    }
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
            insetsController.isAppearanceLightStatusBars = !isDark
            insetsController.isAppearanceLightNavigationBars = !isDark
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
