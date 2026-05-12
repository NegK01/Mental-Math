package com.negk01.mentalmath.ui.screens.home.components

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.domain.model.ThemePreference
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
import com.negk01.mentalmath.ui.theme.OriginalAccent
import com.negk01.mentalmath.ui.theme.OriginalBg
import com.negk01.mentalmath.ui.theme.RoyalAccent
import com.negk01.mentalmath.ui.theme.RoyalBg

private val swatchSize = 44.dp

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun OnboardingModal(
    visible: Boolean,
    selectedTheme: ThemePreference,
    selectedDifficulty: Difficulty,
    onThemeChange: (ThemePreference) -> Unit,
    onDifficultyChange: (Difficulty) -> Unit,
    onDismiss: () -> Unit
) {
    BackHandler(enabled = visible, onBack = onDismiss)

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(initialScale = 0.96f),
        exit = fadeOut() + scaleOut(targetScale = 0.96f)
    ) {
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .pointerInput(Unit) { detectTapGestures { } },
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .heightIn(max = screenHeight * 0.88f)
                    .clip(RoundedCornerShape(28.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
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
                    verticalArrangement = Arrangement.spacedBy(12.dp),
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
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.onboarding_section_difficulty),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Difficulty.entries.forEach { difficulty ->
                            val labelRes = when (difficulty) {
                                Difficulty.EASY -> R.string.difficulty_easy
                                Difficulty.MEDIUM -> R.string.difficulty_medium
                                Difficulty.HARD -> R.string.difficulty_hard
                            }
                            Surface(
                                onClick = { onDifficultyChange(difficulty) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(20.dp),
                                color = if (difficulty == selectedDifficulty) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant
                                }
                            ) {
                                Text(
                                    text = stringResource(labelRes),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (difficulty == selectedDifficulty) {
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    },
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(stringResource(R.string.onboarding_cta))
                }
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
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
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
            .background(bg)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
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
    ThemePreference.SYSTEM        -> SwatchColors(BrandPrimary, Color.White)
    ThemePreference.LIGHT         -> SwatchColors(BackgroundLight, BrandPrimary)
    ThemePreference.ORIGINAL      -> SwatchColors(OriginalBg, OriginalAccent)
    ThemePreference.MENTA_PROFUNDO -> SwatchColors(MentaBg, MentaAccent)
    ThemePreference.OLED          -> SwatchColors(OledBg, OledAccent)
    ThemePreference.NORDIC_FROST  -> SwatchColors(NordicBg, NordicAccent)
    ThemePreference.ROYAL_DARK    -> SwatchColors(RoyalBg, RoyalAccent)
    ThemePreference.GRAPHITE_LIME -> SwatchColors(GraphiteBg, GraphiteAccent)
}
