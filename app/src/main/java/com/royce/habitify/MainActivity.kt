package com.royce.habitify
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val fabAddHabit: FloatingActionButton = findViewById(R.id.fab_add_habit)

        // Handle widget actions
        handleWidgetActions()

        // Load default fragment when app starts
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        // Handle navigation item clicks
        bottomNav.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_todo -> TodoFragment()
                R.id.nav_mood -> MoodFragment()
                R.id.nav_settings -> SettingsFragment()
                else -> HomeFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit()
            true
        }

        // Handle floating action button click
        fabAddHabit.setOnClickListener {
            val addFragment = AddFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, addFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun handleWidgetActions() {
        val action = intent.getStringExtra("action")
        if (action != null) {
            when (action) {
                "add_habit" -> {
                    // Navigate to add habit fragment
                    val addFragment = AddFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, addFragment)
                        .commit()
                }
                "log_mood" -> {
                    // Navigate to mood fragment
                    val moodFragment = MoodFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, moodFragment)
                        .commit()
                }
            }
        }
    }
}

