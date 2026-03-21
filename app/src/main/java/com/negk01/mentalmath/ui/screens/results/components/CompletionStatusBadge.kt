package com.negk01.mentalmath.ui.screens.results.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.negk01.mentalmath.ui.utils.completionStatusToBadgeColor
import com.negk01.mentalmath.ui.utils.completionStatusToDisplayName
import com.negk01.mentalmath.ui.utils.completionStatusToTextColor

@Composable
fun CompletionStatusBadge(
    status: String
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(completionStatusToBadgeColor(status))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = completionStatusToDisplayName(status),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = completionStatusToTextColor(status)
        )
    }
}