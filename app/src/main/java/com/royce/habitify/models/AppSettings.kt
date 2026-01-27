package com.royce.habitify.models

import com.google.gson.annotations.SerializedName

data class AppSettings(
    @SerializedName("hydrationReminderEnabled")
    val hydrationReminderEnabled: Boolean = true,

    @SerializedName("hydrationReminderInterval")
    val hydrationReminderInterval: Int = 60, // minutes

    @SerializedName("dailyHydrationGoal")
    val dailyHydrationGoal: Int = 8, // glasses

    @SerializedName("notificationSoundEnabled")
    val notificationSoundEnabled: Boolean = true,

    @SerializedName("widgetEnabled")
    val widgetEnabled: Boolean = true,

    @SerializedName("theme")
    val theme: String = "system", // system, light, dark

    @SerializedName("firstLaunch")
    val firstLaunch: Boolean = true
)
