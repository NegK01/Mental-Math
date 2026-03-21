package com.negk01.mentalmath.ui.screens.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negk01.mentalmath.ui.theme.Danger
import com.negk01.mentalmath.ui.theme.Primary
import com.negk01.mentalmath.ui.theme.SurfaceCard
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun NumberPad(
    onDigitClick: (String) -> Unit,
    onClearClick: () -> Unit,
    onSubmitClick: () -> Unit,
    isSubmitEnabled: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            NumberPadRow(listOf("1", "2", "3"), onDigitClick)
            NumberPadRow(listOf("4", "5", "6"), onDigitClick)
            NumberPadRow(listOf("7", "8", "9"), onDigitClick)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                KeyButton(
                    text = "C",
                    modifier = Modifier.weight(1f),
                    containerColor = Danger,
                    onClick = onClearClick
                )

                KeyButton(
                    text = "0",
                    modifier = Modifier.weight(1f),
                    containerColor = Primary,
                    onClick = { onDigitClick("0") }
                )

                KeyButton(
                    text = "✓",
                    modifier = Modifier.weight(1f),
                    containerColor = if (isSubmitEnabled) Primary else Color.LightGray,
                    onClick = onSubmitClick
                )
            }
        }
    }
}

@Composable
private fun NumberPadRow(
    digits: List<String>,
    onDigitClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        digits.forEach { digit ->
            KeyButton(
                text = digit,
                modifier = Modifier.weight(1f),
                containerColor = Primary,
                onClick = { onDigitClick(digit) }
            )
        }
    }
}

@Composable
private fun KeyButton(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor)
    ) {
        Text(
            text = text,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}