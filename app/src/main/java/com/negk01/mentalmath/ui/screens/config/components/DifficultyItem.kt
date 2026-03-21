package com.negk01.mentalmath.ui.screens.config.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DifficultyItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    val color = when (text) {
        "Fácil" -> Color(0xFFB7E4C7)
        "Medio" -> Color(0xFFFFE8A1)
        else -> Color(0xFFF8B4B4)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color(0xFFF6F6F8), RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        RadioButton(
            selected = selected,
            onClick = onClick
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(text = text, modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .background(color, CircleShape)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(text = text)
        }
    }
}