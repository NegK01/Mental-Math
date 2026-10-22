package com.negk01.mentalmath.domain.game

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

data class DailyStreakResult(
    val streak: Int,
    val startDate: LocalDate?,
    val endDate: LocalDate?
)

object DailyStreakCalculator {

    fun calculate(
        timestamps: List<Long>,
        zoneId: ZoneId = ZoneId.systemDefault(),
        referenceDate: LocalDate = LocalDate.now(zoneId)
    ): DailyStreakResult {
        if (timestamps.isEmpty()) return DailyStreakResult(0, null, null)

        val playedDays = timestamps
            .map { Instant.ofEpochMilli(it).atZone(zoneId).toLocalDate() }
            .filter { !it.isAfter(referenceDate) }
            .toSet()

        val endDate = when {
            referenceDate in playedDays -> referenceDate
            referenceDate.minusDays(1) in playedDays -> referenceDate.minusDays(1)
            else -> return DailyStreakResult(0, null, null)
        }

        var startDate = endDate
        while (startDate.minusDays(1) in playedDays) {
            startDate = startDate.minusDays(1)
        }

        val streak = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1
        return DailyStreakResult(streak, startDate, endDate)
    }
}
