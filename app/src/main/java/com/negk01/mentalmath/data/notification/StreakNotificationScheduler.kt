package com.negk01.mentalmath.data.notification

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Operation
import androidx.work.WorkManager
import androidx.work.await
import java.time.Duration
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

object StreakNotificationScheduler {

    const val WORK_NAME_STREAK = "mental_math_streak_reminder"
    const val TARGET_HOUR = 20

    internal fun reminderDelayForDaysAheadMillis(
        daysAhead: Int,
        now: ZonedDateTime = ZonedDateTime.now()
    ): Long {
        val target = now.toLocalDate()
            .plusDays(daysAhead.toLong())
            .atTime(TARGET_HOUR, 0)
            .atZone(now.zone)
        val duration = Duration.between(now, target)
        return if (duration.isNegative) 0L else duration.toMillis()
    }

    private fun enqueue(context: Context, delayMillis: Long, policy: ExistingWorkPolicy): Operation {
        val workManager = WorkManager.getInstance(context)
        val request = OneTimeWorkRequestBuilder<StreakReminderWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .build()
        return workManager.enqueueUniqueWork(WORK_NAME_STREAK, policy, request)
    }

    suspend fun appendNextReminderAndAwait(
        context: Context,
        daysAhead: Int,
        now: ZonedDateTime = ZonedDateTime.now()
    ) {
        enqueue(context, reminderDelayForDaysAheadMillis(daysAhead, now), ExistingWorkPolicy.APPEND).await()
    }
}
