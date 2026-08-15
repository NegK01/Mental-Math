package com.negk01.mentalmath.ui.screens.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.domain.model.ThemePreference
import com.negk01.mentalmath.ui.components.AppDialog
import com.negk01.mentalmath.ui.components.OptionSelector
import com.negk01.mentalmath.ui.theme.BackgroundLight
import com.negk01.mentalmath.ui.theme.BrandPrimary
import com.negk01.mentalmath.ui.theme.GraphiteAccent
import com.negk01.mentalmath.ui.theme.GraphiteBg
import com.negk01.mentalmath.ui.theme.MentaAccent
import com.negk01.mentalmath.ui.theme.MentaBg
import com.negk01.mentalmath.ui.theme.NordicAccent
import com.negk01.mentalmath.ui.theme.NordicBg
import com.negk01.mentalmath.ui.theme.OledAccent
import com.negk01.mentalmath.ui.theme.OledBg
import com.negk01.mentalmath.ui.theme.Opacity
import com.negk01.mentalmath.ui.theme.OriginalAccent
import com.negk01.mentalmath.ui.theme.OriginalBg
import com.negk01.mentalmath.ui.theme.Radius
import com.negk01.mentalmath.ui.theme.RoyalAccent
import com.negk01.mentalmath.ui.theme.RoyalBg
import com.negk01.mentalmath.ui.theme.Spacing
import com.negk01.mentalmath.ui.utils.toLabelResId

private val swatchSize = 44.dp

@Composable
fun OnboardingDialog(
    visible: Boolean,
    selectedTheme: ThemePreference,
    selectedDifficulty: Difficulty,
    onThemeChange: (ThemePreference) -> Unit,
    onDifficultyChange: (Difficulty) -> Unit,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(initialScale = 0.96f),
        exit = fadeOut() + scaleOut(targetScale = 0.96f)
    ) {
        AppDialog(
            onDismiss = onDismiss,
            scrollable = true,
            verticalArrangement = Arrangement.spacedBy(Spacing.Lg)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.Xs)
            ) {
                Text(
                    text = stringResource(R.string.onboarding_title),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.onboarding_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(Spacing.Md),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.onboarding_section_theme),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                ThemeSwatchRow(
                    selectedTheme = selectedTheme,
                    onThemeChange = onThemeChange
                )
            }

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(Spacing.Md),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.onboarding_section_difficulty),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                OptionSelector(
                    options = Difficulty.entries,
                    selected = selectedDifficulty,
                    onSelect = onDifficultyChange,
                    optionLabelRes = { it.toLabelResId() },
                    optionShape = RoundedCornerShape(Radius.Lg),
                    optionTextStyle = MaterialTheme.typography.labelLarge,
                    optionFontWeight = null,
                    optionSpacing = Spacing.Sm,
                    optionPadding = PaddingValues(vertical = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(Spacing.Xs))

            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(Radius.Xl)
            ) {
                Text(stringResource(R.string.onboarding_cta))
            }
        }
    }
}

@Composable
private fun ThemeSwatchRow(
    selectedTheme: ThemePreference,
    onThemeChange: (ThemePreference) -> Unit
) {
    val listState = rememberLazyListState()

    Box {
        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(ThemePreference.entries.filter { it != ThemePreference.SYSTEM }) { theme ->
                ThemeSwatch(
                    theme = theme,
                    selected = theme == selectedTheme,
                    onClick = { onThemeChange(theme) }
                )
            }
        }

        AnimatedVisibility(
            visible = listState.canScrollForward,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = 22.dp),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = Opacity.Scrim),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun ThemeSwatch(
    theme: ThemePreference,
    selected: Boolean,
    onClick: () -> Unit
) {
    val (bg, accent) = swatchColorsFor(theme)
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
    val borderWidth = if (selected) 2.5.dp else 1.dp

    Box(
        modifier = Modifier
            .size(swatchSize)
            .clip(CircleShape)
            .border(borderWidth, borderColor, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (theme == ThemePreference.SYSTEM) {
            Row(Modifier.fillMaxSize()) {
                Box(Modifier.weight(1f).fillMaxHeight().background(OriginalBg))
                Box(Modifier.weight(1f).fillMaxHeight().background(BackgroundLight))
            }
        } else {
            Box(Modifier.fillMaxSize().background(bg))
        }
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(accent)
        )
    }
}

private data class SwatchColors(val bg: Color, val accent: Color)

private fun swatchColorsFor(theme: ThemePreference): SwatchColors = when (theme) {
    ThemePreference.SYSTEM        -> SwatchColors(OriginalBg, BrandPrimary)
    ThemePreference.LIGHT         -> SwatchColors(BackgroundLight, BrandPrimary)
    ThemePreference.ORIGINAL      -> SwatchColors(OriginalBg, OriginalAccent)
    ThemePreference.MENTA_PROFUNDO -> SwatchColors(MentaBg, MentaAccent)
    ThemePreference.OLED          -> SwatchColors(OledBg, OledAccent)
    ThemePreference.NORDIC_FROST  -> SwatchColors(NordicBg, NordicAccent)
    ThemePreference.ROYAL_DARK    -> SwatchColors(RoyalBg, RoyalAccent)
    ThemePreference.GRAPHITE_LIME -> SwatchColors(GraphiteBg, GraphiteAccent)
}
