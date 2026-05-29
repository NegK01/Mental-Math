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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.negk01.mentalmath.R
import com.negk01.mentalmath.domain.model.CompletionStatus
import com.negk01.mentalmath.domain.model.Difficulty
import com.negk01.mentalmath.presentation.results.NextGoalState
import com.negk01.mentalmath.ui.components.DifficultyBadge
import com.negk01.mentalmath.ui.theme.Motion
import com.negk01.mentalmath.ui.theme.Spacing
import com.negk01.mentalmath.ui.utils.motionEnabled
import com.negk01.mentalmath.ui.utils.toLabelResId

@Composable
fun ScoreHeroSection(
    correctAnswers: Int,
    totalRounds: Int,
    difficulty: Difficulty?,
    completionStatus: CompletionStatus,
    nextGoal: NextGoalState,
    isNewRecord: Boolean = false,
    modifier: Modifier = Modifier
) {
    val accuracy = if (totalRounds == 0) 0f else correctAnswers.toFloat() / totalRounds

    val animatedProgress = remember { Animatable(0f) }
    val arcScale = remember { Animatable(1f) }
    val motion = motionEnabled()

    LaunchedEffect(Unit) {
        if (motion) {
            animatedProgress.animateTo(
                targetValue = accuracy,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )
        } else {
            animatedProgress.snapTo(accuracy)
        }
    }

    LaunchedEffect(isNewRecord) {
        if (isNewRecord && motion) {
            arcScale.animateTo(1.06f, tween(Motion.Celebration / 2, easing = Motion.EaseEmphasized))
            arcScale.animateTo(1.0f, tween(Motion.Celebration / 2, easing = Motion.EaseEmphasized))
        }
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

    val subtitleText = when (nextGoal) {
        is NextGoalState.LowAccuracy -> stringResource(R.string.results_goal_low,
            nextGoal.targetCorrect,
            nextGoal.totalRounds,
            stringResource(difficulty?.toLabelResId() ?: R.string.difficulty_medium))
        is NextGoalState.StepUp -> stringResource(R.string.results_goal_next,
            stringResource(nextGoal.nextDifficulty.toLabelResId()))
        is NextGoalState.HardKeep -> stringResource(R.string.results_goal_hard_keep)
        is NextGoalState.HardPerfect -> stringResource(R.string.results_goal_hard_perfect)
        is NextGoalState.PerfectNext -> stringResource(R.string.results_goal_perfect_next,
            stringResource(nextGoal.nextDifficulty.toLabelResId()))
    }

    val trackColor = MaterialTheme.colorScheme.surfaceVariant

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.graphicsLayer {
                scaleX = arcScale.value
                scaleY = arcScale.value
            }
        ) {
            Canvas(modifier = Modifier.size(120.dp)) {
                val strokeWidth = 10.dp.toPx()
                val stroke = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                drawArc(color = trackColor, startAngle = -90f, sweepAngle = 360f, useCenter = false, style = stroke)
                drawArc(color = scoreColor, startAngle = -90f, sweepAngle = 360f * animatedProgress.value, useCenter = false, style = stroke)
            }
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = "$correctAnswers", style = MaterialTheme.typography.headlineLarge, color = scoreColor)
                Text(
                    text = "/$totalRounds",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = scoreColor.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 3.dp)
                )
            }
        }

        if (difficulty != null) {
            Spacer(modifier = Modifier.height(Spacing.Md))
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.Sm), verticalAlignment = Alignment.CenterVertically) {
                DifficultyBadge(difficulty = difficulty)
                if (completionStatus == CompletionStatus.ABANDONED) {
                    CompletionStatusBadge(status = completionStatus)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = stringResource(titleRes),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Spacing.Xs))

        Text(
            text = subtitleText,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
