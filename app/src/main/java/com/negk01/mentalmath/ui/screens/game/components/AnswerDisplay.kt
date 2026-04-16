package com.negk01.mentalmath.ui.screens.game.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negk01.mentalmath.R
import com.negk01.mentalmath.ui.theme.Danger
import com.negk01.mentalmath.ui.theme.SuccessText

@Composable
fun AnswerDisplay(
    value: String,
    // null = estado neutro (sin respuesta o animación completada)
    // true = correcta → flash verde
    // false = incorrecta → flash rojo
    lastAnswerCorrect: Boolean? = null
) {
    // tween de 150ms de entrada — rápido para no interrumpir el juego.
    // El reset a null (con delay en el ViewModel) produce la salida suave de vuelta a surface.
    val cardColor by animateColorAsState(
        targetValue = when (lastAnswerCorrect) {
            true  -> SuccessText.copy(alpha = 0.18f)
            false -> Danger.copy(alpha = 0.18f)
            null  -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        },
        animationSpec = tween(durationMillis = 150),
        label = "answerFlash"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Text(
                text = stringResource(R.string.game_answer_title),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 38.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = if (value.isBlank()) {
                        stringResource(R.string.game_answer_placeholder)
                    } else {
                        value
                    },
                    fontSize = if (value.isBlank()) 22.sp else 28.sp,
                    fontWeight = if (value.isBlank()) FontWeight.Normal else FontWeight.Bold,
                    color = if (value.isBlank()) {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f)
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        }
    }
}