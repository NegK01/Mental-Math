package com.negk01.mentalmath.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.negk01.mentalmath.R
import com.negk01.mentalmath.ui.components.AppDialog
import com.negk01.mentalmath.ui.theme.Spacing
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle

private val DaysOfWeek = listOf(
    DayOfWeek.MONDAY,
    DayOfWeek.TUESDAY,
    DayOfWeek.WEDNESDAY,
    DayOfWeek.THURSDAY,
    DayOfWeek.FRIDAY,
    DayOfWeek.SATURDAY,
    DayOfWeek.SUNDAY
)

@Composable
fun StreakCalendarDialog(
    onDismiss: () -> Unit,
    streakStartDate: LocalDate?,
    streakEndDate: LocalDate?
) {
    var currentMonth by remember {
        mutableStateOf(YearMonth.now())
    }

    AppDialog(
        onDismiss = onDismiss,
        dismissOnScrimTap = true,
        verticalArrangement = Arrangement.Top
    ) {
        // Selector de mes y año
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { currentMonth = currentMonth.minusMonths(1) }
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChevronLeft,
                    contentDescription = stringResource(R.string.home_calendar_previous_month),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentMonth.format(
                        DateTimeFormatter.ofPattern(
                            "MMMM yyyy",
                            LocalLocale.current.platformLocale
                        )
                    ).replaceFirstChar { it.uppercase() },
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                val isCurrentMonth = currentMonth == YearMonth.now()
                Text(
                    text = stringResource(R.string.home_calendar_back_to_today),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrentMonth) Color.Transparent else MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable(enabled = !isCurrentMonth) { currentMonth = YearMonth.now() }
                        .padding(horizontal = Spacing.Sm, vertical = 2.dp)
                )
            }

            IconButton(
                onClick = { currentMonth = currentMonth.plusMonths(1) }
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = stringResource(R.string.home_calendar_next_month),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.Md))

        // Días de la semana
        Row(modifier = Modifier.fillMaxWidth()) {
            DaysOfWeek.forEach { day ->
                Text(
                    text = day.getDisplayName(
                        TextStyle.SHORT,
                        LocalLocale.current.platformLocale
                    ).take(2),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.Sm))

        // Grilla estandarizada de 6 semanas
        CalendarGrid(
            month = currentMonth,
            startDate = streakStartDate,
            endDate = streakEndDate
        )
    }
}

@Composable
private fun CalendarGrid(
    month: YearMonth,
    startDate: LocalDate?,
    endDate: LocalDate?
) {
    val firstDayOfMonth = month.atDay(1)

    val firstDayOffset = when (firstDayOfMonth.dayOfWeek) {
        DayOfWeek.MONDAY -> 0
        DayOfWeek.TUESDAY -> 1
        DayOfWeek.WEDNESDAY -> 2
        DayOfWeek.THURSDAY -> 3
        DayOfWeek.FRIDAY -> 4
        DayOfWeek.SATURDAY -> 5
        DayOfWeek.SUNDAY -> 6
    }

    val daysInMonth = month.lengthOfMonth()
    val today = remember { LocalDate.now() }

    // Fijamos siempre 6 semanas (42 celdas) para altura constante sin saltos de UI
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Xs)
    ) {
        repeat(6) { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.Xs)
            ) {
                repeat(7) { column ->
                    val cellIndex = row * 7 + column
                    val dayNumber = cellIndex - firstDayOffset + 1

                    if (dayNumber in 1..daysInMonth) {
                        val date = month.atDay(dayNumber)
                        val isToday = date == today
                        val isStart = startDate != null && date == startDate
                        val isEnd = endDate != null && date == endDate
                        val isInRange = startDate != null && endDate != null &&
                                !date.isBefore(startDate) && !date.isAfter(endDate)

                        CalendarDay(
                            date = date,
                            isToday = isToday,
                            isStart = isStart,
                            isEnd = isEnd,
                            isInRange = isInRange,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDay(
    date: LocalDate,
    isToday: Boolean,
    isStart: Boolean,
    isEnd: Boolean,
    isInRange: Boolean,
    modifier: Modifier = Modifier
) {
    val isBoundary = isStart || isEnd

    val backgroundColor = when {
        isBoundary -> MaterialTheme.colorScheme.primary
        isInRange -> MaterialTheme.colorScheme.primary.copy(alpha = 0.20f)
        else -> Color.Transparent
    }

    val textColor = when {
        isBoundary -> MaterialTheme.colorScheme.onPrimary
        isInRange || isToday -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isToday || isInRange) FontWeight.Bold else FontWeight.Normal,
            color = textColor
        )
    }
}
