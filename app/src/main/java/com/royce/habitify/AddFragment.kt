package com.royce.habitify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.royce.habitify.data.DataManager
import com.royce.habitify.models.Habit
import org.threeten.bp.LocalDateTime
import java.util.*

class AddFragment : Fragment() {

    private lateinit var dataManager: DataManager
    private lateinit var goalInput: TextInputEditText
    private lateinit var habitNameInput: TextInputEditText
    private lateinit var periodDropdown: AutoCompleteTextView
    private lateinit var habitTypeDropdown: AutoCompleteTextView
    private lateinit var createButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize DataManager
        dataManager = DataManager.getInstance(requireContext())

        // Initialize UI components
        initializeViews(view)
        setupDropdowns()
        setupCreateButton()
        setupCloseButton(view)
    }

    private fun initializeViews(view: View) {
        goalInput = view.findViewById<TextInputLayout>(R.id.your_goal_layout).editText as TextInputEditText
        habitNameInput = view.findViewById<TextInputLayout>(R.id.habit_name_layout).editText as TextInputEditText
        periodDropdown = view.findViewById(R.id.spinner_period)
        habitTypeDropdown = view.findViewById(R.id.spinner_habit_type)
        createButton = view.findViewById(R.id.btn_create_habit)
    }

    private fun setupDropdowns() {
        // Dropdown data
        val periods = listOf("Daily", "Weekly", "Monthly")
        val habitTypes = listOf("Health", "Study", "Work", "Leisure")

        // Period dropdown
        val periodAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, periods)
        periodDropdown.setAdapter(periodAdapter)
        periodDropdown.setText(periods[0], false) // Set default to "Daily"

        // Habit type dropdown
        val habitTypeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, habitTypes)
        habitTypeDropdown.setAdapter(habitTypeAdapter)
        habitTypeDropdown.setText(habitTypes[0], false) // Set default to "Health"
    }

    private fun setupCreateButton() {
        createButton.setOnClickListener {
            if (validateInputs()) {
                createHabit()
            }
        }
    }

    private fun setupCloseButton(view: View) {
        // The close button is an ImageButton in the title layout
        view.findViewById<View>(R.id.btn_close).setOnClickListener {
            // Navigate back to previous fragment using activity's fragment manager
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun validateInputs(): Boolean {
        val goal = goalInput.text?.toString()?.trim()
        val habitName = habitNameInput.text?.toString()?.trim()
        val period = periodDropdown.text?.toString()?.trim()
        val habitType = habitTypeDropdown.text?.toString()?.trim()

        return when {
            goal.isNullOrEmpty() -> {
                showError("Please enter your goal")
                goalInput.requestFocus()
                false
            }
            habitName.isNullOrEmpty() -> {
                showError("Please enter habit name")
                habitNameInput.requestFocus()
                false
            }
            period.isNullOrEmpty() -> {
                showError("Please select a period")
                periodDropdown.requestFocus()
                false
            }
            habitType.isNullOrEmpty() -> {
                showError("Please select habit type")
                habitTypeDropdown.requestFocus()
                false
            }
            else -> true
        }
    }

    private fun createHabit() {
        try {
            val goal = goalInput.text?.toString()?.trim() ?: ""
            val habitName = habitNameInput.text?.toString()?.trim() ?: ""
            val period = periodDropdown.text?.toString()?.trim() ?: "Daily"
            val habitType = habitTypeDropdown.text?.toString()?.trim() ?: "Health"

            // Create new habit
            val newHabit = Habit(
                id = UUID.randomUUID().toString(),
                title = habitName,
                description = goal,
                period = period,
                habitType = habitType,
                createdDate = LocalDateTime.now(),
                isCompleted = false,
                completedDates = mutableListOf(),
                targetCompletions = if (period == "Daily") 1 else if (period == "Weekly") 7 else 30,
                currentStreak = 0
            )

            // Save habit using DataManager
            val existingHabits = dataManager.getHabits().toMutableList()
            existingHabits.add(newHabit)
            dataManager.saveHabits(existingHabits)

            // Show success message
            Toast.makeText(requireContext(), "Habit created successfully!", Toast.LENGTH_SHORT).show()

            // Navigate back to TodoFragment
            requireActivity().supportFragmentManager.popBackStack()

        } catch (e: Exception) {
            showError("Failed to create habit: ${e.message}")
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
