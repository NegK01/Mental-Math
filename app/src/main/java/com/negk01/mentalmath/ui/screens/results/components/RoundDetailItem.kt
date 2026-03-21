package com.negk01.mentalmath.ui.screens.results.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negk01.mentalmath.domain.model.RoundDetail
import com.negk01.mentalmath.ui.theme.Danger
import com.negk01.mentalmath.ui.theme.EasyText
import com.negk01.mentalmath.ui.theme.SurfaceMuted
import com.negk01.mentalmath.ui.theme.TextPrimary
import com.negk01.mentalmath.ui.theme.TextSecondary

@Composable
fun RoundDetailItem(
    item: RoundDetail
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceMuted)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${item.expression} → ${item.userAnswer}",
            modifier = Modifier.weight(1f),
            fontSize = 18.sp,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.size(8.dp))

        Icon(
            imageVector = if (item.isCorrect) Icons.Default.Check else Icons.Default.Close,
            contentDescription = null,
            tint = if (item.isCorrect) EasyText else Danger
        )

        Spacer(modifier = Modifier.size(6.dp))

        Text(
            text = item.time,
            fontSize = 16.sp,
            color = TextSecondary
        )
    }
}