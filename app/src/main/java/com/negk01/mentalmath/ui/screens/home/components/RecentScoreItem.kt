package com.negk01.mentalmath.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negk01.mentalmath.domain.model.GameRecord
import com.negk01.mentalmath.ui.theme.EasyBadge
import com.negk01.mentalmath.ui.theme.EasyText
import com.negk01.mentalmath.ui.theme.HardBadge
import com.negk01.mentalmath.ui.theme.HardText
import com.negk01.mentalmath.ui.theme.MediumBadge
import com.negk01.mentalmath.ui.theme.MediumText
import com.negk01.mentalmath.ui.theme.SurfaceMuted
import com.negk01.mentalmath.ui.theme.TextMuted
import com.negk01.mentalmath.ui.theme.TextPrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.text.font.FontWeight

@Composable
fun RecentScoreItem(
    record: GameRecord
) {
    val badgeColor = when (record.difficulty.lowercase()) {
        "fácil" -> EasyBadge
        "medio" -> MediumBadge
        else -> HardBadge
    }

    val badgeTextColor = when (record.difficulty.lowercase()) {
        "fácil" -> EasyText
        "medio" -> MediumText
        else -> HardText
    }

    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        .format(Date(record.playedAt))

    val averageSeconds = record.averageResponseTimeMillis / 1000.0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceMuted)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(badgeColor)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = record.difficulty,
                fontSize = 13.sp,
                color = badgeTextColor,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = formattedDate,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            color = TextMuted
        )

        Text(
            text = "${record.correctAnswers}/${record.totalRounds} (${String.format("%.1fs", averageSeconds)})",
            fontSize = 14.sp,
            color = TextPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}