package com.royce.habitify.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.royce.habitify.models.AppSettings
import com.royce.habitify.models.Habit
import com.royce.habitify.models.MoodEntry
import java.time.LocalDate
import java.util.*

class DataManager private constructor(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "wellness_app_prefs"
        private const val KEY_HABITS = "habits_json"
        private const val KEY_MOOD_ENTRIES = "mood_entries_json"
        private const val KEY_SETTINGS = "settings_json"

        @Volatile
        private var instance: DataManager? = null

        fun getInstance(context: Context): DataManager {
            return instance ?: synchronized(this) {
                instance ?: DataManager(context.applicationContext).also { instance = it }
            }
        }
    }

    // ==================== HABIT MANAGEMENT ====================

    fun getHabits(): List<Habit> {
        val json = prefs.getString(KEY_HABITS, null)
        return if (json != null) {
            try {
                val type = object : TypeToken<List<Habit>>() {}.type
                gson.fromJson(json, type) ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    fun saveHabits(habits: List<Habit>) {
        val json = gson.toJson(habits)
        prefs.edit().putString(KEY_HABITS, json).apply()
    }

    fun addHabit(habit: Habit) {
        val currentHabits = getHabits().toMutableList()
        currentHabits.add(habit)
        saveHabits(currentHabits)
    }

    fun updateHabit(updatedHabit: Habit) {
        val currentHabits = getHabits().toMutableList()
        val index = currentHabits.indexOfFirst { it.id == updatedHabit.id }
        if (index != -1) {
            currentHabits[index] = updatedHabit
            saveHabits(currentHabits)
        }
    }

    fun deleteHabit(habitId: String) {
        val currentHabits = getHabits().toMutableList()
        currentHabits.removeAll { it.id == habitId }
        saveHabits(currentHabits)
    }

    fun getHabitById(habitId: String): Habit? {
        return getHabits().find { it.id == habitId }
    }

    fun toggleHabitCompletion(habitId: String): Boolean {
        val habit = getHabitById(habitId) ?: return false
        val updatedHabit = habit.toggleCompletion()
        updateHabit(updatedHabit)
        return updatedHabit.isCompleted
    }

    fun getTodaysHabits(): List<Habit> {
        return getHabits().filter { it.period == "Daily" }
    }

    fun getTodaysCompletionPercentage(): Int {
        val todaysHabits = getTodaysHabits()
        if (todaysHabits.isEmpty()) return 0

        val completedCount = todaysHabits.count { it.isCompletedToday() }
        return (completedCount * 100) / todaysHabits.size
    }

    // ==================== MOOD ENTRY MANAGEMENT ====================

    fun getMoodEntries(): List<MoodEntry> {
        val json = prefs.getString(KEY_MOOD_ENTRIES, null)
        return if (json != null) {
            try {
                val type = object : TypeToken<List<MoodEntry>>() {}.type
                val moodEntries: List<MoodEntry> = gson.fromJson(json, type) ?: emptyList()
                moodEntries.sortedByDescending { it.dateTime }
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    fun saveMoodEntries(moodEntries: List<MoodEntry>) {
        val json = gson.toJson(moodEntries)
        prefs.edit().putString(KEY_MOOD_ENTRIES, json).apply()
    }

    fun addMoodEntry(moodEntry: MoodEntry) {
        val currentEntries = getMoodEntries().toMutableList()
        currentEntries.add(moodEntry)
        saveMoodEntries(currentEntries)
    }

    fun deleteMoodEntry(entryId: String) {
        val currentEntries = getMoodEntries().toMutableList()
        currentEntries.removeAll { it.id == entryId }
        saveMoodEntries(currentEntries)
    }

    fun getMoodEntriesForDate(date: LocalDate): List<MoodEntry> {
        return getMoodEntries().filter { it.dateTime.toLocalDate() == date }
    }

    fun getRecentMoodEntries(limit: Int = 7): List<MoodEntry> {
        return getMoodEntries().take(limit)
    }

    // ==================== SETTINGS MANAGEMENT ====================

    fun getSettings(): AppSettings {
        val json = prefs.getString(KEY_SETTINGS, null)
        return if (json != null) {
            try {
                gson.fromJson(json, AppSettings::class.java) ?: AppSettings()
            } catch (e: Exception) {
                AppSettings()
            }
        } else {
            AppSettings()
        }
    }

    fun saveSettings(settings: AppSettings) {
        val json = gson.toJson(settings)
        prefs.edit().putString(KEY_SETTINGS, json).apply()
    }

    fun updateSettings(updater: (AppSettings) -> AppSettings) {
        val currentSettings = getSettings()
        val updatedSettings = updater(currentSettings)
        saveSettings(updatedSettings)
    }

    // ==================== UTILITY METHODS ====================

    fun clearAllData() {
        prefs.edit().clear().apply()
    }

    fun exportData(): String {
        val data = mapOf(
            "habits" to getHabits(),
            "moodEntries" to getMoodEntries(),
            "settings" to getSettings()
        )
        return gson.toJson(data)
    }

    fun importData(jsonData: String): Boolean {
        return try {
            val type = object : TypeToken<Map<String, Any>>() {}.type
            val data: Map<String, Any> = gson.fromJson(jsonData, type)

            // Import habits
            if (data.containsKey("habits")) {
                val habitsJson = gson.toJson(data["habits"])
                prefs.edit().putString(KEY_HABITS, habitsJson).apply()
            }

            // Import mood entries
            if (data.containsKey("moodEntries")) {
                val moodJson = gson.toJson(data["moodEntries"])
                prefs.edit().putString(KEY_MOOD_ENTRIES, moodJson).apply()
            }

            // Import settings
            if (data.containsKey("settings")) {
                val settingsJson = gson.toJson(data["settings"])
                prefs.edit().putString(KEY_SETTINGS, settingsJson).apply()
            }

            true
        } catch (e: Exception) {
            false
        }
    }
}
