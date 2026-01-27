package com.royce.habitify

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Temporarily use simple layout to test
        return inflater.inflate(R.layout.fragment_settings_simple, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Debug: Show that settings fragment is loading
        android.widget.Toast.makeText(requireContext(), "Settings Fragment Loaded", android.widget.Toast.LENGTH_SHORT).show()

        // Setup toolbar
        setupToolbar(view)
        
        // Setup all click listeners
        setupClickListeners(view)
        
        // Setup switches
        setupSwitches(view)
    }

    private fun setupToolbar(view: View) {
        // Simple layout doesn't have toolbar, so skip this
        // val toolbar = view.findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        // toolbar?.setNavigationIcon(null)
    }

    private fun setupClickListeners(view: View) {
        // Profile card
        view.findViewById<View>(R.id.card_profile)?.setOnClickListener {
            showAccountSettings()
        }

        // Hydration settings (in preferences section) - simplified approach
        // We'll handle this through a separate click listener on the preferences card
        view.findViewById<View>(R.id.card_preferences)?.setOnClickListener {
            navigateToHydrationSettings()
        }

        // Data management buttons
        view.findViewById<MaterialButton>(R.id.btn_export_data)?.setOnClickListener {
            exportData()
        }

        view.findViewById<MaterialButton>(R.id.btn_clear_data)?.setOnClickListener {
            clearAllData()
        }

        // About section - simplified approach
        view.findViewById<View>(R.id.card_about)?.setOnClickListener {
            showAboutApp()
        }

        // Logout button
        view.findViewById<MaterialButton>(R.id.btn_logout)?.setOnClickListener {
            handleSignOut()
        }
    }

    private fun setupSwitches(view: View) {
        val habitRemindersSwitch = view.findViewById<SwitchMaterial>(R.id.switch_habit_reminders)
        val moodRemindersSwitch = view.findViewById<SwitchMaterial>(R.id.switch_mood_reminders)

        // Set default values
        habitRemindersSwitch?.isChecked = true
        moodRemindersSwitch?.isChecked = true

        // Add listeners
        habitRemindersSwitch?.setOnCheckedChangeListener { _, isChecked ->
            android.widget.Toast.makeText(
                requireContext(),
                if (isChecked) "Habit reminders enabled" else "Habit reminders disabled",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }

        moodRemindersSwitch?.setOnCheckedChangeListener { _, isChecked ->
            android.widget.Toast.makeText(
                requireContext(),
                if (isChecked) "Mood reminders enabled" else "Mood reminders disabled",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun navigateToHydrationSettings() {
        val hydrationFragment = HydrationSettingsFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, hydrationFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showAccountSettings() {
        android.widget.Toast.makeText(requireContext(), "Profile editing coming soon!", android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun showAboutApp() {
        android.widget.Toast.makeText(requireContext(), "Habitify v1.0\nWellness App for Lab 3\nBuilt with ❤️", android.widget.Toast.LENGTH_LONG).show()
    }

    private fun exportData() {
        android.widget.Toast.makeText(requireContext(), "Data export feature coming soon!", android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun clearAllData() {
        android.widget.Toast.makeText(requireContext(), "Clear data feature coming soon!", android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun handleSignOut() {
        // Navigate back to GetStartedActivity
        val intent = Intent(requireContext(), GetStartedActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}
