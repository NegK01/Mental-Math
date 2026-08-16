package com.negk01.mentalmath.ui.screens.game.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negk01.mentalmath.R
import com.negk01.mentalmath.ui.theme.Opacity
import com.negk01.mentalmath.ui.theme.Radius
import com.negk01.mentalmath.ui.theme.Spacing

private val Row1Digits = listOf("1", "2", "3")
private val Row2Digits = listOf("4", "5", "6")
private val Row3Digits = listOf("7", "8", "9")

@Composable
fun NumberPad(
    onDigitClick: (String) -> Unit,
    onClearClick: () -> Unit,
    onSubmitClick: () -> Unit,
    isSubmitEnabled: Boolean,
    buttonHeight: Dp = 64.dp
) {
    val buttonSpacing = if (buttonHeight <= 50.dp) Spacing.Sm else 10.dp
    val digitFontSize = if (buttonHeight <= 50.dp) 18.sp else 22.sp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Radius.Md))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(Spacing.Md),
            verticalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            NumberPadRow(Row1Digits, onDigitClick, buttonHeight, digitFontSize, buttonSpacing)
            NumberPadRow(Row2Digits, onDigitClick, buttonHeight, digitFontSize, buttonSpacing)
            NumberPadRow(Row3Digits, onDigitClick, buttonHeight, digitFontSize, buttonSpacing)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                ActionButton(
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.error.copy(alpha = Opacity.BadgeContainer),
                    contentColor = MaterialTheme.colorScheme.error,
                    buttonHeight = buttonHeight,
                    onClick = onClearClick
                )
                KeyButton(
                    text = "0",
                    modifier = Modifier.weight(1f),
                    buttonHeight = buttonHeight,
                    fontSize = digitFontSize,
                    onClick = { onDigitClick("0") }
                )
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
    fontSize: TextUnit,
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
    fontSize: TextUnit,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptic = LocalHapticFeedback.current

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessHigh),
        label = "keyScale"
    )

    Button(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onClick()
        },
        modifier = modifier.height(buttonHeight).graphicsLayer { scaleX = scale; scaleY = scale },
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Text(text = text, fontSize = fontSize, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ActionButton(
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color,
    buttonHeight: Dp,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptic = LocalHapticFeedback.current

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessHigh),
        label = "actionScale"
    )

    Button(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onClick()
        },
        modifier = modifier.height(buttonHeight).graphicsLayer { scaleX = scale; scaleY = scale },
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor, contentColor = contentColor)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Backspace,
            contentDescription = stringResource(R.string.game_clear_short)
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
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptic = LocalHapticFeedback.current

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessHigh),
        label = "submitScale"
    )

    Button(
        onClick = {
            if (isEnabled) {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick()
            } else {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        },
        enabled = true,
        modifier = modifier
            .height(buttonHeight)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .then(if (!isEnabled) Modifier.semantics { disabled() } else Modifier),
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Icon(imageVector = Icons.Default.Check, contentDescription = stringResource(R.string.game_submit_answer))
    }
}
