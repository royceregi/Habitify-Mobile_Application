package com.royce.habitify.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.royce.habitify.MainActivity
import com.royce.habitify.R
import com.royce.habitify.data.DataManager
import com.royce.habitify.models.Habit
import com.royce.habitify.models.MoodEntry
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

class WellnessWidgetProvider : AppWidgetProvider() {

    companion object {
        const val ACTION_ADD_HABIT = "com.royce.habitify.ACTION_ADD_HABIT"
        const val ACTION_LOG_MOOD = "com.royce.habitify.ACTION_LOG_MOOD"
        const val ACTION_REFRESH_WIDGET = "com.royce.habitify.ACTION_REFRESH_WIDGET"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        
        when (intent.action) {
            ACTION_ADD_HABIT -> {
                // Open app to add habit
                val appIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("action", "add_habit")
                }
                context.startActivity(appIntent)
            }
            ACTION_LOG_MOOD -> {
                // Open app to mood journal
                val appIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("action", "log_mood")
                }
                context.startActivity(appIntent)
            }
            ACTION_REFRESH_WIDGET -> {
                // Refresh widget data
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(
                    android.content.ComponentName(context, WellnessWidgetProvider::class.java)
                )
                onUpdate(context, appWidgetManager, appWidgetIds)
            }
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_wellness)
        
        // Update time
        val currentTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
        views.setTextViewText(R.id.widget_time, currentTime)
        
        // Load and update habit data
        updateHabitData(context, views)
        
        // Load and update mood data
        updateMoodData(context, views)
        
        // Set up click listeners
        setupClickListeners(context, views)
        
        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun updateHabitData(context: Context, views: RemoteViews) {
        try {
            val dataManager = DataManager.getInstance(context)
            val habits = dataManager.getHabits()
            val todaysHabits = habits.filter { it.period == "Daily" }
            
            if (todaysHabits.isEmpty()) {
                views.setTextViewText(R.id.widget_progress_text, "No habits yet")
                views.setProgressBar(R.id.widget_progress_bar, 100, 0, false)
                views.setTextViewText(R.id.widget_percentage, "0%")
                views.setTextViewText(R.id.widget_streak_count, "0")
                return
            }
            
            val completedCount = todaysHabits.count { it.isCompletedToday() }
            val completionPercentage = if (todaysHabits.isNotEmpty()) {
                (completedCount * 100) / todaysHabits.size
            } else 0
            
            // Update progress
            views.setTextViewText(
                R.id.widget_progress_text,
                "$completedCount/${todaysHabits.size} Habits Completed"
            )
            views.setProgressBar(R.id.widget_progress_bar, 100, completionPercentage, false)
            views.setTextViewText(R.id.widget_percentage, "$completionPercentage%")
            
            // Calculate streak (simplified)
            val streak = calculateStreak(todaysHabits)
            views.setTextViewText(R.id.widget_streak_count, streak.toString())
            
        } catch (e: Exception) {
            // Handle errors gracefully
            views.setTextViewText(R.id.widget_progress_text, "Error loading data")
            views.setProgressBar(R.id.widget_progress_bar, 100, 0, false)
            views.setTextViewText(R.id.widget_percentage, "0%")
            views.setTextViewText(R.id.widget_streak_count, "0")
        }
    }

    private fun updateMoodData(context: Context, views: RemoteViews) {
        try {
            val dataManager = DataManager.getInstance(context)
            val moodEntries = dataManager.getMoodEntries()
            val today = LocalDateTime.now().toLocalDate()
            
            val todaysMood = moodEntries.firstOrNull { entry ->
                entry.dateTime.toLocalDate() == today
            }
            
            if (todaysMood != null) {
                views.setTextViewText(R.id.widget_mood_emoji, todaysMood.emoji)
            } else {
                views.setTextViewText(R.id.widget_mood_emoji, "😐")
            }
            
        } catch (e: Exception) {
            views.setTextViewText(R.id.widget_mood_emoji, "😐")
        }
    }

    private fun setupClickListeners(context: Context, views: RemoteViews) {
        // Main widget click - open app
        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val mainPendingIntent = PendingIntent.getActivity(
            context, 0, mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_progress_bar, mainPendingIntent)
        
        // Add habit button
        val addHabitIntent = Intent(context, WellnessWidgetProvider::class.java).apply {
            action = ACTION_ADD_HABIT
        }
        val addHabitPendingIntent = PendingIntent.getBroadcast(
            context, 1, addHabitIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_add_habit, addHabitPendingIntent)
        
        // Log mood button
        val logMoodIntent = Intent(context, WellnessWidgetProvider::class.java).apply {
            action = ACTION_LOG_MOOD
        }
        val logMoodPendingIntent = PendingIntent.getBroadcast(
            context, 2, logMoodIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_log_mood, logMoodPendingIntent)
    }

    private fun calculateStreak(habits: List<Habit>): Int {
        // Simplified streak calculation
        // In a real implementation, you'd calculate based on consecutive completion days
        val today = LocalDateTime.now()
        var streak = 0
        
        // Check last 30 days for consecutive completions
        for (i in 0..29) {
            val checkDate = today.minusDays(i.toLong()).toLocalDate()
            val completedHabitsOnDate = habits.count { habit ->
                habit.completedDates.contains(checkDate)
            }
            
            if (completedHabitsOnDate > 0) {
                streak++
            } else {
                break
            }
        }
        
        return streak
    }
}
