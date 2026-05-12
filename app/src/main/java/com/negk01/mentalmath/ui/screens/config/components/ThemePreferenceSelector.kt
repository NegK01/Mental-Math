package com.negk01.mentalmath.ui.screens.config.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.ThemePreference
import com.negk01.mentalmath.ui.theme.BackgroundDark
import com.negk01.mentalmath.ui.theme.BackgroundLight
import com.negk01.mentalmath.ui.theme.BrandPrimary
import com.negk01.mentalmath.ui.theme.GraphiteAccent
import com.negk01.mentalmath.ui.theme.GraphiteBg
import com.negk01.mentalmath.ui.theme.GraphiteSurface
import com.negk01.mentalmath.ui.theme.MentaAccent
import com.negk01.mentalmath.ui.theme.MentaBg
import com.negk01.mentalmath.ui.theme.MentaSurface
import com.negk01.mentalmath.ui.theme.NordicAccent
import com.negk01.mentalmath.ui.theme.NordicBg
import com.negk01.mentalmath.ui.theme.NordicSurface
import com.negk01.mentalmath.ui.theme.OledAccent
import com.negk01.mentalmath.ui.theme.OledBg
import com.negk01.mentalmath.ui.theme.OledSurface
import com.negk01.mentalmath.ui.theme.OriginalAccent
import com.negk01.mentalmath.ui.theme.OriginalBg
import com.negk01.mentalmath.ui.theme.OriginalSurface
import com.negk01.mentalmath.ui.theme.RoyalAccent
import com.negk01.mentalmath.ui.theme.RoyalBg
import com.negk01.mentalmath.ui.theme.RoyalSurface
import com.negk01.mentalmath.ui.theme.SurfaceDark
import com.negk01.mentalmath.ui.theme.SurfaceLight
import com.negk01.mentalmath.ui.utils.toLabelResId

private data class ThemePreviewColors(
    val bg: Color,
    val surface: Color,
    val accent: Color,
    val labelColor: Color = Color.White.copy(alpha = 0.75f)
)

private fun ThemePreference.previewColors(): ThemePreviewColors = when (this) {
    ThemePreference.SYSTEM -> ThemePreviewColors(
        bg = BackgroundDark,
        surface = SurfaceDark,
        accent = BrandPrimary,
        labelColor = Color.White.copy(alpha = 0.75f)
    )
    ThemePreference.LIGHT -> ThemePreviewColors(
        bg = BackgroundLight,
        surface = SurfaceLight,
        accent = BrandPrimary,
        labelColor = Color.Black.copy(alpha = 0.65f)
    )
    ThemePreference.ORIGINAL -> ThemePreviewColors(OriginalBg, OriginalSurface, OriginalAccent)
    ThemePreference.MENTA_PROFUNDO -> ThemePreviewColors(MentaBg, MentaSurface, MentaAccent)
    ThemePreference.OLED -> ThemePreviewColors(OledBg, OledSurface, OledAccent)
    ThemePreference.NORDIC_FROST -> ThemePreviewColors(NordicBg, NordicSurface, NordicAccent)
    ThemePreference.ROYAL_DARK -> ThemePreviewColors(RoyalBg, RoyalSurface, RoyalAccent)
    ThemePreference.GRAPHITE_LIME -> ThemePreviewColors(GraphiteBg, GraphiteSurface, GraphiteAccent)
}

@Composable
fun ThemePreferenceSelector(
    selected: ThemePreference,
    onSelect: (ThemePreference) -> Unit
) {
    val allThemes = listOf(
        ThemePreference.SYSTEM,
        ThemePreference.LIGHT,
        ThemePreference.ORIGINAL,
        ThemePreference.MENTA_PROFUNDO,
        ThemePreference.OLED,
        ThemePreference.NORDIC_FROST,
        ThemePreference.ROYAL_DARK,
        ThemePreference.GRAPHITE_LIME
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = stringResource(R.string.config_theme_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            allThemes.chunked(2).forEach { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    pair.forEach { theme ->
                        ThemePreviewCard(
                            modifier = Modifier.weight(1f),
                            label = stringResource(theme.toLabelResId()),
                            colors = theme.previewColors(),
                            isSelected = selected == theme,
                            onClick = { onSelect(theme) },
                            bgContent = if (theme == ThemePreference.SYSTEM) {
                                {
                                    Box(
                                        Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(0.5f)
                                            .background(BackgroundDark)
                                    )
                                    Box(
                                        Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(0.5f)
                                            .align(Alignment.TopEnd)
                                            .background(BackgroundLight)
                                    )
                                }
                            } else null
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemePreviewCard(
    modifier: Modifier = Modifier,
    label: String,
    colors: ThemePreviewColors,
    isSelected: Boolean,
    onClick: () -> Unit,
    bgContent: (@Composable BoxScope.() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .height(68.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) colors.accent else colors.accent.copy(alpha = 0.25f),
                shape = RoundedCornerShape(12.dp)
            )
            .background(colors.bg)
            .clickable(onClick = onClick)
    ) {
        if (bgContent != null) {
            Box(modifier = Modifier.fillMaxSize(), content = bgContent)
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-8).dp, y = 8.dp)
                .size(10.dp)
                .clip(CircleShape)
                .background(colors.accent)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .align(Alignment.BottomCenter)
                .background(colors.surface)
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = colors.labelColor,
                maxLines = 1
            )
        }
    }
}
