package com.negk01.mentalmath.ui.screens.game.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.negk01.mentalmath.presentation.game.TimerUrgency
import com.negk01.mentalmath.ui.theme.Motion
import com.negk01.mentalmath.ui.theme.Spacing
import com.negk01.mentalmath.R

// Nota: 'difficulty' eliminado — nunca se renderizó y el mockup no lo incluye.
// Si en el futuro se necesita, se puede añadir como subtítulo bajo el timer.
@Composable
fun GameProgressCard(
    currentRound: Int,
    totalRounds: Int,
    timeLeftSeconds: Int,
    timerUrgency: TimerUrgency
) {
    // ── Progreso de ronda animado ─────────────────────────────────────────────
    // tween de 400ms evita el salto visual brusco entre rondas
    val animatedProgress by animateFloatAsState(
        targetValue = currentRound.toFloat() / totalRounds.toFloat(),
        animationSpec = tween(durationMillis = Motion.Slow),
        label = "roundProgress"
    )

    // ── Color del timer animado ───────────────────────────────────────────────
    // Transición suave: tertiary (normal) → secondary (advertencia) → error (urgente)
    val timerColor by animateColorAsState(
        targetValue = when (timerUrgency) {
            TimerUrgency.CRITICAL -> MaterialTheme.colorScheme.error
            TimerUrgency.WARNING  -> MaterialTheme.colorScheme.secondary
            TimerUrgency.NORMAL   -> MaterialTheme.colorScheme.tertiary
        },
        animationSpec = tween(durationMillis = Motion.Fast),
        label = "timerColor"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Spacing.Sm)
    ) {
        // Barra de progreso delgada y plana — sin Card, sin elevación
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(Spacing.Xs),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Spacer(modifier = Modifier.height(Spacing.Sm))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.game_round_progress, currentRound, totalRounds),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Timer visible como texto con color semántico animado
            Text(
                text = stringResource(R.string.common_seconds_int, timeLeftSeconds),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = timerColor
            )
        }
    }
}