package com.royce.habitify.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.royce.habitify.MainActivity
import com.royce.habitify.R

class NotificationReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "hydration_reminders"
        const val NOTIFICATION_ID = 1001
        const val ACTION_HYDRATION_REMINDER = "com.royce.habitify.HYDRATION_REMINDER"
        const val ACTION_MARK_HYDRATED = "com.royce.habitify.MARK_HYDRATED"
        
        private const val CHANNEL_NAME = "Hydration Reminders"
        private const val CHANNEL_DESCRIPTION = "Reminders to drink water throughout the day"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_HYDRATION_REMINDER -> {
                createNotificationChannel(context)
                showHydrationNotification(context)
            }
            ACTION_MARK_HYDRATED -> {
                // Handle mark as hydrated action
                markAsHydrated(context)
            }
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
                setShowBadge(true)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showHydrationNotification(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Create "Mark as Hydrated" action
        val hydratedIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = ACTION_MARK_HYDRATED
        }
        val hydratedPendingIntent = PendingIntent.getBroadcast(
            context, 0, hydratedIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("💧 Time to Hydrate!")
            .setContentText("Stay healthy - drink some water! Your body will thank you.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .addAction(
                R.drawable.ic_check,
                "Mark as Hydrated",
                hydratedPendingIntent
            )
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("💧 Hydration Reminder\n\n" +
                        "It's time to drink some water! Staying hydrated is essential for:\n" +
                        "• Better focus and energy\n" +
                        "• Improved mood\n" +
                        "• Healthy skin\n" +
                        "• Better digestion\n\n" +
                        "Tap to open the app or use the action button to mark as hydrated."))
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, notification)
        }
    }

    private fun markAsHydrated(context: Context) {
        // Cancel the notification
        with(NotificationManagerCompat.from(context)) {
            cancel(NOTIFICATION_ID)
        }

        // Here you could also log the hydration event to SharedPreferences
        // For now, we'll just show a toast
        android.widget.Toast.makeText(
            context,
            "Great job staying hydrated! 💧",
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }
}
