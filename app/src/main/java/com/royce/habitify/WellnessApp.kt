package com.royce.habitify

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class WellnessApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize ThreeTenABP for java.time support
        AndroidThreeTen.init(this)
    }
}
