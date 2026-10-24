package com.negk01.mentalmath.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.negk01.mentalmath.MainActivity
import com.negk01.mentalmath.R

object NotificationHelper {

    const val CHANNEL_ID_STREAK = "streak_reminders_channel"
    const val NOTIFICATION_ID_STREAK = 1001

    fun createNotificationChannel(context: Context) {
        val name = context.getString(R.string.notification_channel_streak_name)
        val descriptionText = context.getString(R.string.notification_channel_streak_description)

        val channel = NotificationChannel(
            CHANNEL_ID_STREAK,
            name,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = descriptionText
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun cancelStreakNotification(context: Context) {
        val manager = NotificationManagerCompat.from(context)
        manager.cancel(NOTIFICATION_ID_STREAK)
    }

    fun showStreakNotification(
        context: Context,
        title: String,
        message: String
    ): Boolean {
        val manager = NotificationManagerCompat.from(context)
        if (!areNotificationsEnabled(context)) return false

        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_STREAK,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val largeIcon = BitmapFactory.decodeResource(context.resources, R.mipmap.new_ic_logo_launcher)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_STREAK)
            .setSmallIcon(R.mipmap.ic_notification)
            .setLargeIcon(largeIcon)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        return try {
            manager.notify(NOTIFICATION_ID_STREAK, notification)
            true
        } catch (e: SecurityException) {
            Log.w("NotificationHelper", "Notification permission changed before posting", e)
            false
        }
    }

    fun areNotificationsEnabled(context: Context): Boolean {
        val manager = context.getSystemService(NotificationManager::class.java)
        return manager.areNotificationsEnabled() &&
            manager.getNotificationChannel(CHANNEL_ID_STREAK)?.importance != NotificationManager.IMPORTANCE_NONE
    }
}
