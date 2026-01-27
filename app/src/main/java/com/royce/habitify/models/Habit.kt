package com.royce.habitify.models

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

data class Habit(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("title")
    val title: String = "",

    @SerializedName("description")
    val description: String = "",

    @SerializedName("period")
    val period: String = "Daily", // Daily, Weekly, Monthly

    @SerializedName("habitType")
    val habitType: String = "Health", // Health, Study, Work, Leisure

    @SerializedName("createdDate")
    val createdDate: LocalDateTime = LocalDateTime.now(),

    @SerializedName("isCompleted")
    val isCompleted: Boolean = false,

    @SerializedName("completedDates")
    val completedDates: MutableList<LocalDate> = mutableListOf(),

    @SerializedName("targetCompletions")
    val targetCompletions: Int = 1, // For weekly/monthly habits

    @SerializedName("currentStreak")
    val currentStreak: Int = 0
) {
    // Helper method to check if completed today
    fun isCompletedToday(): Boolean {
        val today = LocalDate.now()
        return completedDates.contains(today)
    }

    // Helper method to get completion percentage for today
    fun getCompletionPercentage(): Int {
        return if (isCompleted) 100 else 0
    }

    // Helper method to toggle completion for today
    fun toggleCompletion(): Habit {
        val today = LocalDate.now()
        val newCompletedDates = completedDates.toMutableList()

        if (newCompletedDates.contains(today)) {
            newCompletedDates.remove(today)
        } else {
            newCompletedDates.add(today)
        }

        return copy(
            completedDates = newCompletedDates,
            isCompleted = newCompletedDates.contains(today)
        )
    }
}
