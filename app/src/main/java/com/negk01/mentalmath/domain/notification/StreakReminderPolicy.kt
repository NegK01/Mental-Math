package com.negk01.mentalmath.domain.notification

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

enum class ReminderStage(val priority: Int) {
    AT_RISK(1),
    BROKEN(2),
    RETENTION(3),
    SUNSET(4)
}

data class ReminderProgress(
    val lastPlayedAt: Long,
    val stagePriority: Int
)

data class ReminderDecision(
    val stage: ReminderStage? = null,
    val rescheduleDaysAhead: Int? = null
)

object StreakReminderPolicy {
    fun calculateDaysSinceLastPlayed(
        lastPlayedTimestamp: Long,
        today: LocalDate,
        zoneId: ZoneId
    ): Long = ChronoUnit.DAYS.between(
        Instant.ofEpochMilli(lastPlayedTimestamp).atZone(zoneId).toLocalDate(),
        today
    )

    fun evaluateDecision(
        notificationsEnabled: Boolean,
        lastPlayedTimestamp: Long?,
        today: LocalDate,
        zoneId: ZoneId,
        progress: ReminderProgress? = null
    ): ReminderDecision {
        if (!notificationsEnabled || lastPlayedTimestamp == null) return ReminderDecision()

        val previousPriority = progress
            ?.takeIf { it.lastPlayedAt == lastPlayedTimestamp }
            ?.stagePriority ?: 0
        if (previousPriority >= ReminderStage.SUNSET.priority) return ReminderDecision()

        val days = calculateDaysSinceLastPlayed(lastPlayedTimestamp, today, zoneId)
        val decision = when {
            days <= 0L -> ReminderDecision(rescheduleDaysAhead = 1)
            days == 1L -> ReminderDecision(ReminderStage.AT_RISK, 1)
            days == 2L -> ReminderDecision(ReminderStage.BROKEN, 5)
            days in 3L..7L -> ReminderDecision(ReminderStage.RETENTION, (21L - days).toInt())
            days in 8L..21L -> ReminderDecision(ReminderStage.SUNSET)
            else -> ReminderDecision()
        }
        return decision.copy(
            stage = decision.stage?.takeIf { it.priority > previousPriority }
        )
    }
}
