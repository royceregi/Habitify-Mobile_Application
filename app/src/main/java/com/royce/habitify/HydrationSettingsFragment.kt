package com.royce.habitify

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.royce.habitify.managers.HydrationManager
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class HydrationSettingsFragment : Fragment() {

    private lateinit var hydrationManager: HydrationManager
    private lateinit var switchHydrationEnabled: SwitchMaterial
    private lateinit var tvStatusSubtitle: TextView
    private lateinit var tvNextReminder: TextView
    private lateinit var tvRemindersToday: TextView
    private lateinit var tvWaterGlasses: TextView
    private lateinit var etInterval: TextInputEditText
    private lateinit var btnInterval1h: MaterialButton
    private lateinit var btnInterval2h: MaterialButton
    private lateinit var btnInterval3h: MaterialButton
    private lateinit var btnSaveSettings: MaterialButton

    private var currentInterval = 120L // Default 2 hours in minutes

    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hydration_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize HydrationManager
        hydrationManager = HydrationManager(requireContext())

        // Initialize UI components
        initializeViews(view)
        setupClickListeners()
        loadCurrentSettings()
        updateUI()
    }

    private fun initializeViews(view: View) {
        switchHydrationEnabled = view.findViewById(R.id.switch_hydration_enabled)
        tvStatusSubtitle = view.findViewById(R.id.tv_status_subtitle)
        tvNextReminder = view.findViewById(R.id.tv_next_reminder)
        tvRemindersToday = view.findViewById(R.id.tv_reminders_today)
        tvWaterGlasses = view.findViewById(R.id.tv_water_glasses)
        etInterval = view.findViewById(R.id.et_interval)
        btnInterval1h = view.findViewById(R.id.btn_interval_1h)
        btnInterval2h = view.findViewById(R.id.btn_interval_2h)
        btnInterval3h = view.findViewById(R.id.btn_interval_3h)
        btnSaveSettings = view.findViewById(R.id.btn_save_hydration_settings)
    }

    private fun setupClickListeners() {
        switchHydrationEnabled.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkNotificationPermissionAndStartReminders()
            } else {
                stopHydrationReminders()
            }
        }

        btnInterval1h.setOnClickListener { setInterval(60) }
        btnInterval2h.setOnClickListener { setInterval(120) }
        btnInterval3h.setOnClickListener { setInterval(180) }

        btnSaveSettings.setOnClickListener { saveSettings() }
    }

    private fun setInterval(minutes: Long) {
        currentInterval = minutes
        etInterval.setText(minutes.toString())
        updateIntervalButtons()
    }

    private fun updateIntervalButtons() {
        // Reset all buttons
        btnInterval1h.isSelected = false
        btnInterval2h.isSelected = false
        btnInterval3h.isSelected = false

        // Select current interval button
        when (currentInterval) {
            60L -> btnInterval1h.isSelected = true
            120L -> btnInterval2h.isSelected = true
            180L -> btnInterval3h.isSelected = true
        }
    }

    private fun loadCurrentSettings() {
        // Load saved settings from SharedPreferences
        val prefs = requireContext().getSharedPreferences("hydration_settings", 0)
        currentInterval = prefs.getLong("interval_minutes", 120L)
        
        etInterval.setText(currentInterval.toString())
        updateIntervalButtons()
    }

    private fun saveSettings() {
        val intervalText = etInterval.text?.toString()?.trim()
        if (intervalText.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please enter a valid interval", Toast.LENGTH_SHORT).show()
            return
        }

        val interval = intervalText.toLongOrNull()
        if (interval == null || interval < 30 || interval > 480) {
            Toast.makeText(requireContext(), "Please enter an interval between 30 and 480 minutes", Toast.LENGTH_SHORT).show()
            return
        }

        currentInterval = interval
        updateIntervalButtons()

        // Save settings
        val prefs = requireContext().getSharedPreferences("hydration_settings", 0)
        prefs.edit().putLong("interval_minutes", currentInterval).apply()

        // Restart reminders with new interval if enabled
        if (switchHydrationEnabled.isChecked) {
            hydrationManager.startHydrationReminders(currentInterval)
        }

        Toast.makeText(requireContext(), "Settings saved successfully!", Toast.LENGTH_SHORT).show()
        updateUI()
    }

    private fun checkNotificationPermissionAndStartReminders() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
                return
            }
        }
        startHydrationReminders()
    }

    private fun startHydrationReminders() {
        hydrationManager.startHydrationReminders(currentInterval)
        Toast.makeText(requireContext(), "Hydration reminders started! 💧", Toast.LENGTH_SHORT).show()
        updateUI()
    }

    private fun stopHydrationReminders() {
        hydrationManager.stopHydrationReminders()
        Toast.makeText(requireContext(), "Hydration reminders stopped", Toast.LENGTH_SHORT).show()
        updateUI()
    }

    private fun updateUI() {
        val isEnabled = hydrationManager.isHydrationRemindersEnabled()
        switchHydrationEnabled.isChecked = isEnabled

        if (isEnabled) {
            tvStatusSubtitle.text = "Every ${formatInterval(currentInterval)}"
            val nextReminder = hydrationManager.getNextReminderTime()
            tvNextReminder.text = "Next reminder: ${formatTime(nextReminder)}"
            tvNextReminder.visibility = View.VISIBLE
        } else {
            tvStatusSubtitle.text = "Reminders disabled"
            tvNextReminder.visibility = View.GONE
        }

        // Mock data for demonstration
        tvRemindersToday.text = "5"
        tvWaterGlasses.text = "8"
    }

    private fun formatInterval(minutes: Long): String {
        return when {
            minutes < 60 -> "${minutes}m"
            minutes % 60 == 0L -> "${minutes / 60}h"
            else -> "${minutes / 60}h ${minutes % 60}m"
        }
    }

    private fun formatTime(dateTime: LocalDateTime?): String {
        return dateTime?.format(DateTimeFormatter.ofPattern("h:mm a")) ?: "Unknown"
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startHydrationReminders()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Notification permission is required for hydration reminders",
                    Toast.LENGTH_LONG
                ).show()
                switchHydrationEnabled.isChecked = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }
}
