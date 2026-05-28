package com.negk01.mentalmath.ui.screens.results.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.ui.components.DifficultyBadge

@Composable
fun ScoreHeroSection(
    correctAnswers: Int,
    totalRounds: Int,
    difficulty: Difficulty?,
    modifier: Modifier = Modifier
) {
    val accuracy = if (totalRounds == 0) 0f else correctAnswers.toFloat() / totalRounds

    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animatedProgress.animateTo(
            targetValue = accuracy,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
    }

    val scoreColor = when {
        accuracy >= 0.9f -> MaterialTheme.colorScheme.tertiary
        accuracy >= 0.6f -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.secondary
    }

    val titleRes = when {
        accuracy >= 0.9f -> R.string.results_feedback_title_great
        accuracy >= 0.6f -> R.string.results_feedback_title_good
        else -> R.string.results_feedback_title_keep
    }
    val subtitleRes = when {
        accuracy >= 0.9f -> R.string.results_feedback_subtitle_great
        accuracy >= 0.6f -> R.string.results_feedback_subtitle_good
        else -> R.string.results_feedback_subtitle_keep
    }

    val trackColor = MaterialTheme.colorScheme.surfaceVariant

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(120.dp)) {
                val strokeWidth = 10.dp.toPx()
                val stroke = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                drawArc(
                    color = trackColor,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = stroke
                )
                drawArc(
                    color = scoreColor,
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress.value,
                    useCenter = false,
                    style = stroke
                )
            }
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "$correctAnswers",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = scoreColor
                )
                Text(
                    text = "/$totalRounds",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = scoreColor.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 3.dp)
                )
            }
        }

        if (difficulty != null) {
            Spacer(modifier = Modifier.height(12.dp))
            DifficultyBadge(difficulty = difficulty)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = stringResource(titleRes),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(subtitleRes),
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
