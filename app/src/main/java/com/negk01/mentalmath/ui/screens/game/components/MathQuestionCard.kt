package com.negk01.mentalmath.ui.screens.game.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import com.negk01.mentalmath.ui.theme.Motion
import com.negk01.mentalmath.ui.theme.Radius
import com.negk01.mentalmath.ui.theme.Spacing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MathQuestionCard(
    questionText: String,
    questionFontSize: TextUnit = 34.sp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Radius.Dialog))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = Spacing.Xl),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AnimatedContent(
                    targetState = questionText,
                    transitionSpec = {
                        (slideInHorizontally(tween(Motion.Medium)) { width -> width / 2 } + fadeIn(tween(Motion.Medium))).togetherWith(
                            slideOutHorizontally(tween(Motion.Medium)) { width -> -width / 2 } + fadeOut(tween(Motion.Medium))
                        )
                    },
                    label = "questionTransition"
                ) { text ->
                    Text(
                        text = text,
                        fontSize = questionFontSize,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
