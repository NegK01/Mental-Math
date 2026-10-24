package com.negk01.mentalmath.data.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.negk01.mentalmath.R
import com.negk01.mentalmath.data.local.db.AppDatabase
import com.negk01.mentalmath.data.local.db.DatabaseProvider
import com.negk01.mentalmath.data.local.entity.ReminderProgressEntity
import com.negk01.mentalmath.domain.game.DailyStreakCalculator
import com.negk01.mentalmath.domain.model.AppSettings
import com.negk01.mentalmath.domain.notification.ReminderProgress
import com.negk01.mentalmath.domain.notification.ReminderStage
import com.negk01.mentalmath.domain.notification.StreakReminderPolicy
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import java.time.Clock
import java.time.LocalDate

class StreakReminderWorker @JvmOverloads constructor(
    appContext: Context,
    workerParams: WorkerParameters,
    private val db: AppDatabase = DatabaseProvider.getDatabase(appContext),
    private val clock: Clock = Clock.systemDefaultZone()
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val settings = db.settingsDao().getSettings()
        val notificationsEnabled = settings?.notificationsEnabled ?: AppSettings.default().notificationsEnabled
        if (!notificationsEnabled) return Result.success()

        val lastPlayedAt = db.gameRecordDao().getLastPlayedTimestamp() ?: return Result.success()
        val progress = db.reminderProgressDao().getProgress()?.let {
            ReminderProgress(it.lastPlayedAt, it.stagePriority)
        }
        val zoneId = clock.zone
        val today = LocalDate.now(clock)
        val decision = StreakReminderPolicy.evaluateDecision(
            notificationsEnabled = notificationsEnabled,
            lastPlayedTimestamp = lastPlayedAt,
            today = today,
            zoneId = zoneId,
            progress = progress
        )

        decision.stage?.let { stage ->
            val streak = if (stage == ReminderStage.AT_RISK) {
                DailyStreakCalculator.calculate(
                    db.gameRecordDao().getAllPlayedTimestamps(), zoneId, today
                ).streak
            } else {
                0
            }
            if (db.gameRecordDao().getLastPlayedTimestamp() != lastPlayedAt ||
                db.settingsDao().getSettings()?.notificationsEnabled == false
            ) return Result.success()

            currentCoroutineContext().ensureActive()
            if (showNotification(stage, streak)) {
                db.reminderProgressDao().saveProgress(
                    ReminderProgressEntity(lastPlayedAt = lastPlayedAt, stagePriority = stage.priority)
                )
            }
        }

        currentCoroutineContext().ensureActive()
        decision.rescheduleDaysAhead?.let { days ->
            StreakNotificationScheduler.appendNextReminderAndAwait(applicationContext, days)
        }
        return Result.success()
    }

    private fun showNotification(stage: ReminderStage, streak: Int): Boolean {
        val titleRes = when (stage) {
            ReminderStage.AT_RISK -> R.string.notification_streak_stage1_title
            ReminderStage.BROKEN -> R.string.notification_streak_stage2_title
            ReminderStage.RETENTION -> R.string.notification_streak_stage3_title
            ReminderStage.SUNSET -> R.string.notification_streak_stage4_title
        }
        val message = when (stage) {
            ReminderStage.AT_RISK -> if (streak <= 1) {
                applicationContext.getString(R.string.notification_streak_stage1_message_one)
            } else {
                applicationContext.getString(R.string.notification_streak_stage1_message, streak)
            }
            ReminderStage.BROKEN -> applicationContext.getString(R.string.notification_streak_stage2_message)
            ReminderStage.RETENTION -> applicationContext.getString(R.string.notification_streak_stage3_message)
            ReminderStage.SUNSET -> applicationContext.getString(R.string.notification_streak_stage4_message)
        }
        return NotificationHelper.showStreakNotification(
            applicationContext,
            applicationContext.getString(titleRes),
            message
        )
    }
}
