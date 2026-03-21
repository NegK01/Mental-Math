package com.negk01.mentalmath.ui.screens.results.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negk01.mentalmath.ui.theme.Primary
import com.negk01.mentalmath.ui.theme.SurfaceMuted
import com.negk01.mentalmath.ui.theme.TextSecondary
import com.negk01.mentalmath.ui.theme.EasyText

@Composable
fun ResultStatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    isSuccess: Boolean
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceMuted)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = TextSecondary
        )

        Text(
            text = value,
            fontSize = 22.sp,
            color = if (isSuccess) EasyText else Primary
        )
    }
}