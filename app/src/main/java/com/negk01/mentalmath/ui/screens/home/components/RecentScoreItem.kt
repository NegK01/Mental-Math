package com.negk01.mentalmath.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.negk01.mentalmath.domain.model.Score

@Composable
fun RecentScoreItem(
    score: Score
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF6F6F8))
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color(0xFFF29B9B))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = score.difficulty,
                fontSize = 13.sp,
                color = Color(0xFF7A2E2E),
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = score.date,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            color = Color(0xFF7C8593),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "${score.result} (${score.time})",
            fontSize = 14.sp,
            color = Color(0xFF374151),
            fontWeight = FontWeight.Medium
        )
    }
}