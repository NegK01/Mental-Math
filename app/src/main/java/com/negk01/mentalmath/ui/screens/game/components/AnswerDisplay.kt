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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negk01.mentalmath.ui.theme.Danger
import com.negk01.mentalmath.ui.theme.SuccessText
import com.negk01.mentalmath.ui.utils.isDarkTheme
import com.negk01.mentalmath.ui.utils.motionEnabled

@Composable
fun AnswerDisplay(
    value: String,
    lastAnswerCorrect: Boolean? = null
) {
    val cardColor by animateColorAsState(
        targetValue = when (lastAnswerCorrect) {
            true  -> SuccessText.copy(alpha = 0.18f)
            false -> Danger.copy(alpha = 0.18f)
            null  -> if (isDarkTheme()) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surfaceVariant
        },
        animationSpec = tween(durationMillis = 150),
        label = "answerFlash"
    )

    val motion = motionEnabled()
    val infiniteTransition = rememberInfiniteTransition(label = "cursor")
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursorAlpha"
    )
    val effectiveCursorAlpha = if (!motion) 1f else cursorAlpha

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
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
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
            Box(
                modifier = Modifier
                    .padding(start = if (value.isNotBlank()) 3.dp else 0.dp)
                    .width(2.dp)
                    .height(32.dp)
                    .alpha(effectiveCursorAlpha)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            )
        }
    }
}
