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
import com.negk01.mentalmath.R
import com.negk01.mentalmath.ui.components.AppDialog
import com.negk01.mentalmath.ui.theme.Radius
import com.negk01.mentalmath.ui.theme.Spacing
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle

private val DaysOfWeek = DayOfWeek.entries

@Composable
fun StreakCalendarDialog(
    onDismiss: () -> Unit,
    today: LocalDate,
    streakStartDate: LocalDate?,
    streakEndDate: LocalDate?
) {
    var currentMonth by remember {
        mutableStateOf(YearMonth.from(today))
    }
    val locale = LocalLocale.current.platformLocale
    val monthFormatter = remember(locale) { DateTimeFormatter.ofPattern("MMMM yyyy", locale) }
    val monthTitle = remember(currentMonth, monthFormatter) {
        currentMonth.format(monthFormatter).replaceFirstChar { it.titlecase(locale) }
    }

    AppDialog(
        onDismiss = onDismiss,
        dismissOnScrimTap = true,
        verticalArrangement = Arrangement.Top
    ) {
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
                    text = monthTitle,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                val isCurrentMonth = currentMonth == YearMonth.from(today)
                Text(
                    text = stringResource(R.string.home_calendar_back_to_today),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrentMonth) Color.Transparent else MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(Radius.Sm))
                        .clickable(enabled = !isCurrentMonth) {
                            currentMonth = YearMonth.from(today)
                        }
                        .padding(horizontal = Spacing.Sm, vertical = Spacing.Xs)
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Xs)
        ) {
            DaysOfWeek.forEach { day ->
                Text(
                    text = day.getDisplayName(
                        TextStyle.SHORT,
                        locale
                    ).take(3).replaceFirstChar { it.titlecase(locale) },
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.Sm))

        CalendarGrid(
            month = currentMonth,
            today = today,
            startDate = streakStartDate,
            endDate = streakEndDate
        )
    }
}

@Composable
private fun CalendarGrid(
    month: YearMonth,
    today: LocalDate,
    startDate: LocalDate?,
    endDate: LocalDate?
) {
    val daysInMonth = month.lengthOfMonth()
    val firstDayOffset = month.atDay(1).dayOfWeek.value - 1

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
                        val isBoundary = date == startDate || date == endDate
                        val isInRange = startDate != null && endDate != null &&
                                date in startDate..endDate

                        CalendarDay(
                            dayNumber = dayNumber,
                            isToday = isToday,
                            isBoundary = isBoundary,
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
    dayNumber: Int,
    isToday: Boolean,
    isBoundary: Boolean,
    isInRange: Boolean,
    modifier: Modifier = Modifier
) {
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
            text = dayNumber.toString(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isToday || isInRange) FontWeight.Bold else FontWeight.Normal,
            color = textColor
        )
    }
}
