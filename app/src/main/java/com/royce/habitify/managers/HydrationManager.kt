package com.royce.habitify.managers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.royce.habitify.receivers.NotificationReceiver
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.util.*

class HydrationManager(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    companion object {
        const val REQUEST_CODE_HYDRATION = 1001
        private const val DEFAULT_INTERVAL_MINUTES = 120L // 2 hours
    }

    fun startHydrationReminders(intervalMinutes: Long = DEFAULT_INTERVAL_MINUTES) {
        // Cancel existing reminders first
        stopHydrationReminders()

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = NotificationReceiver.ACTION_HYDRATION_REMINDER
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_HYDRATION,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Calculate first reminder time (next hour)
        val now = LocalDateTime.now()
        val nextHour = now.plusHours(1).withMinute(0).withSecond(0)
        val triggerTime = nextHour.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        // Schedule repeating alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                intervalMinutes * 60 * 1000, // Convert minutes to milliseconds
                pendingIntent
            )
        } else {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                intervalMinutes * 60 * 1000,
                pendingIntent
            )
        }
    }

    fun stopHydrationReminders() {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = NotificationReceiver.ACTION_HYDRATION_REMINDER
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_HYDRATION,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
    }

    fun isHydrationRemindersEnabled(): Boolean {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = NotificationReceiver.ACTION_HYDRATION_REMINDER
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_HYDRATION,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        return pendingIntent != null
    }

    fun getNextReminderTime(): LocalDateTime? {
        // This is a simplified implementation
        // In a real app, you might want to store the next reminder time
        return LocalDateTime.now().plusHours(2)
    }
}
