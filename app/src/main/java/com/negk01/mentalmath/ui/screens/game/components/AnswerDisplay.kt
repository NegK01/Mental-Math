package com.negk01.mentalmath.ui.screens.game.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.negk01.mentalmath.ui.theme.Motion
import com.negk01.mentalmath.ui.theme.Opacity
import com.negk01.mentalmath.ui.theme.Radius
import com.negk01.mentalmath.ui.theme.Spacing
import com.negk01.mentalmath.ui.utils.isDarkTheme

@Composable
fun AnswerDisplay(
    value: String,
    lastAnswerCorrect: Boolean? = null
) {
    val cardColor by animateColorAsState(
        targetValue = when (lastAnswerCorrect) {
            true  -> MaterialTheme.colorScheme.tertiary.copy(alpha = Opacity.BadgeContainer)
            false -> MaterialTheme.colorScheme.error.copy(alpha = Opacity.BadgeContainer)
            null  -> if (isDarkTheme()) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surfaceVariant
        },
        animationSpec = tween(durationMillis = Motion.Fast),
        label = "answerFlash"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "cursor")
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(Motion.ExtraSlow, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursorAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Radius.Xl))
            .background(cardColor)
            .padding(horizontal = 18.dp, vertical = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 42.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (value.isNotBlank()) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
            Box(
                modifier = Modifier
                    .padding(start = if (value.isNotBlank()) 3.dp else 0.dp)
                    .width(2.dp)
                    .height(Spacing.Xxl)
                    .alpha(cursorAlpha)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            )
        }
    }
}
