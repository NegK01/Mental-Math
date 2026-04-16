package com.negk01.mentalmath.ui.screens.game.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// questionFontSize: viene de BoxWithConstraints en GameScreen para ser adaptativo.
// Default de 34.sp para previews y usos standalone.
@Composable
fun MathQuestionCard(
    questionText: String,
    questionFontSize: TextUnit = 34.sp
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {


                // AnimatedContent: crossfade de 180ms al cambiar la pregunta entre rondas.
                // Duración corta para no interrumpir el flow de juego,
                // pero suficiente para que el usuario note el cambio visualmente.
                AnimatedContent(
                    targetState = questionText,
                    transitionSpec = {
                        (slideInHorizontally(tween(250)) { width -> width / 2 } + fadeIn(tween(250))).togetherWith(
                            slideOutHorizontally(tween(250)) { width -> -width / 2 } + fadeOut(tween(250))
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