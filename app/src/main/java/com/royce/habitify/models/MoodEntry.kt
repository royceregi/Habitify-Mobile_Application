package com.royce.habitify.models

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

data class MoodEntry(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("emoji")
    val emoji: String = "😊",

    @SerializedName("moodText")
    val moodText: String = "", // Optional description

    @SerializedName("dateTime")
    val dateTime: LocalDateTime = LocalDateTime.now(),

    @SerializedName("notes")
    val notes: String = "" // Additional notes
) {
    // Helper method to get formatted date string
    fun getFormattedDate(): String {
        val now = LocalDateTime.now()
        val today = now.toLocalDate()
        val entryDate = dateTime.toLocalDate()

        return when {
            entryDate == today -> "Today"
            entryDate == today.minusDays(1) -> "Yesterday"
            else -> entryDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
        }
    }

    // Helper method to get formatted time string
    fun getFormattedTime(): String {
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    // Helper method to get mood category based on emoji
    fun getMoodCategory(): String {
        return when (emoji) {
            "😊", "😄", "😁" -> "Happy"
            "😐", "😕" -> "Neutral"
            "😢", "😭", "😔" -> "Sad"
            "😠", "😡", "🤬" -> "Angry"
            "😴", "😪" -> "Tired"
            "🤔", "🤨" -> "Thoughtful"
            "😍", "🥰" -> "Loving"
            "😎", "🤩" -> "Excited"
            else -> "Other"
        }
    }
}
