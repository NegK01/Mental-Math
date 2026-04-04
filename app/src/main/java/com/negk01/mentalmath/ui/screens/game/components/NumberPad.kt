
//
//                SubmitButton(
//                    modifier = Modifier.weight(1f),
//                    isEnabled = isSubmitEnabled,
//                    onClick = onSubmitClick
//                )
//            }
//        }
//    }
//}

//

package com.negk01.mentalmath.ui.screens.game.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negk01.mentalmath.R

// buttonHeight viene de BoxWithConstraints en GameScreen — adaptativo por pantalla.
// Default de 64.dp para previews y usos standalone.
@Composable
fun NumberPad(
    onDigitClick: (String) -> Unit,
    onClearClick: () -> Unit,
    onSubmitClick: () -> Unit,
    isSubmitEnabled: Boolean,
    buttonHeight: Dp = 64.dp
) {
    // Espaciado entre botones: proporcional a la altura del botón para mantener densidad visual
    val buttonSpacing = if (buttonHeight <= 50.dp) 8.dp else 10.dp

    // fontSize proporcional al botón — legible en todos los tamaños
    val digitFontSize = if (buttonHeight <= 50.dp) 18.sp else 22.sp
    val actionFontSize = if (buttonHeight <= 50.dp) 16.sp else 20.sp

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            NumberPadRow(
                digits = listOf("1", "2", "3"),
                onDigitClick = onDigitClick,
                buttonHeight = buttonHeight,
                fontSize = digitFontSize,
                spacing = buttonSpacing
            )
            NumberPadRow(
                digits = listOf("4", "5", "6"),
                onDigitClick = onDigitClick,
                buttonHeight = buttonHeight,
                fontSize = digitFontSize,
                spacing = buttonSpacing
            )
            NumberPadRow(
                digits = listOf("7", "8", "9"),
                onDigitClick = onDigitClick,
                buttonHeight = buttonHeight,
                fontSize = digitFontSize,
                spacing = buttonSpacing
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                // C — prominente en rojo, jerarquía visual clara
                ActionButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.game_clear_short),
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    buttonHeight = buttonHeight,
                    fontSize = actionFontSize,
                    onClick = onClearClick
                )

                KeyButton(
                    text = "0",
                    modifier = Modifier.weight(1f),
                    buttonHeight = buttonHeight,
                    fontSize = digitFontSize,
                    onClick = { onDigitClick("0") }
                )

                // ✓ — mismo estilo neutro que los números.
                // El rojo del C ya establece la jerarquía de "peligro/acción destructiva",
                // por lo que el ✓ no necesita primary color para diferenciarse.
                SubmitButton(
                    modifier = Modifier.weight(1f),
                    isEnabled = isSubmitEnabled,
                    buttonHeight = buttonHeight,
                    onClick = onSubmitClick
                )
            }
        }
    }
}

@Composable
private fun NumberPadRow(
    digits: List<String>,
    onDigitClick: (String) -> Unit,
    buttonHeight: Dp,
    fontSize: androidx.compose.ui.unit.TextUnit,
    spacing: Dp
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        digits.forEach { digit ->
            KeyButton(
                text = digit,
                modifier = Modifier.weight(1f),
                buttonHeight = buttonHeight,
                fontSize = fontSize,
                onClick = { onDigitClick(digit) }
            )
        }
    }
}

@Composable
private fun KeyButton(
    text: String,
    modifier: Modifier = Modifier,
    buttonHeight: Dp,
    fontSize: androidx.compose.ui.unit.TextUnit,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Scale spring: comprime al presionar, rebota al soltar — feedback táctil visual
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "keyScale"
    )

    Button(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onClick()
        },
        modifier = modifier
            .height(buttonHeight)
            .graphicsLayer { scaleX = scale; scaleY = scale },
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ActionButton(
    modifier: Modifier = Modifier,
    text: String,
    containerColor: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color,
    buttonHeight: Dp,
    fontSize: androidx.compose.ui.unit.TextUnit,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "actionScale"
    )

    Button(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onClick()
        },
        modifier = modifier
            .height(buttonHeight)
            .graphicsLayer { scaleX = scale; scaleY = scale },
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SubmitButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    buttonHeight: Dp,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "submitScale"
    )

    Button(
        onClick = {
            if (isEnabled) haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onClick()
        },
        enabled = isEnabled,
        modifier = modifier
            .height(buttonHeight)
            .graphicsLayer { scaleX = scale; scaleY = scale },
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = stringResource(R.string.game_submit_answer)
        )
    }
}