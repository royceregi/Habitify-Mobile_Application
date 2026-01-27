package com.royce.habitify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.royce.habitify.adapter.HabitPreviewAdapter
import com.royce.habitify.data.DataManager
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class HomeFragment : Fragment() {

    private lateinit var dataManager: DataManager
    private lateinit var habitPreviewAdapter: HabitPreviewAdapter

    // Views for progress overview
    private lateinit var tvCompletionPercentage: TextView
    private lateinit var tvCurrentDate: TextView
    private lateinit var progressBar: LinearProgressIndicator
    private lateinit var tvCompletedCount: TextView
    private lateinit var tvTotalHabits: TextView
    private lateinit var tvStreakCount: TextView

    // Views for habits preview
    private lateinit var rvTodayHabitsPreview: RecyclerView
    private lateinit var emptyHabitsState: LinearLayout

    // Action buttons
    private lateinit var btnAddHabit: View
    private lateinit var btnMoodJournal: View
    private lateinit var btnHydration: View
    private lateinit var btnSettings: View
    private lateinit var btnViewAll: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataManager = DataManager.getInstance(requireContext())

        initializeViews(view)
        setupHabitPreviewRecyclerView()
        setupClickListeners()
        updateDashboard()
        loadTodaysHabits()
    }

    private fun initializeViews(view: View) {
        // Progress overview views
        tvCompletionPercentage = view.findViewById(R.id.tv_completion_percentage)
        tvCurrentDate = view.findViewById(R.id.tv_current_date)
        progressBar = view.findViewById(R.id.progress_bar)
        tvCompletedCount = view.findViewById(R.id.tv_completed_count)
        tvTotalHabits = view.findViewById(R.id.tv_total_habits)
        tvStreakCount = view.findViewById(R.id.tv_streak_count)

        // Habits preview views
        rvTodayHabitsPreview = view.findViewById(R.id.rv_today_habits_preview)
        emptyHabitsState = view.findViewById(R.id.empty_habits_state)

        // Action buttons
        btnAddHabit = view.findViewById(R.id.btn_add_habit)
        btnMoodJournal = view.findViewById(R.id.btn_mood_journal)
        btnHydration = view.findViewById(R.id.btn_hydration)
        btnSettings = view.findViewById(R.id.btn_settings)
        btnViewAll = view.findViewById(R.id.btn_view_all)
    }

    private fun setupHabitPreviewRecyclerView() {
        habitPreviewAdapter = HabitPreviewAdapter { habit ->
            navigateToHabitDetails(habit)
        }

        rvTodayHabitsPreview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = habitPreviewAdapter
        }
    }

    private fun setupClickListeners() {
        btnAddHabit.setOnClickListener { navigateToAddHabit() }
        btnMoodJournal.setOnClickListener { navigateToMoodJournal() }
        btnHydration.setOnClickListener { navigateToHydration() }
        btnSettings.setOnClickListener { navigateToSettings() }
        btnViewAll.setOnClickListener { navigateToHabitsList() }
    }

    private fun updateDashboard() {
        val todaysHabits = dataManager.getTodaysHabits()
        val completionPercentage = dataManager.getTodaysCompletionPercentage()
        val completedCount = todaysHabits.count { it.isCompletedToday() }

        // Update current date
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd")
        tvCurrentDate.text = today.format(formatter)

        // Update progress
        tvCompletionPercentage.text = "$completionPercentage%"
        progressBar.progress = completionPercentage

        // Update stats
        tvCompletedCount.text = completedCount.toString()
        tvTotalHabits.text = todaysHabits.size.toString()
        tvStreakCount.text = calculateCurrentStreak().toString()
    }

    private fun loadTodaysHabits() {
        val todaysHabits = dataManager.getTodaysHabits()

        if (todaysHabits.isEmpty()) {
            rvTodayHabitsPreview.visibility = View.GONE
            emptyHabitsState.visibility = View.VISIBLE
        } else {
            rvTodayHabitsPreview.visibility = View.VISIBLE
            emptyHabitsState.visibility = View.GONE
            habitPreviewAdapter.submitList(todaysHabits)
        }
    }

    private fun calculateCurrentStreak(): Int {
        // Simplified streak calculation
        // In a real app, this would track streaks across multiple days
        val today = LocalDate.now()
        var streak = 0

        // For now, just return a mock streak based on completion
        val todaysHabits = dataManager.getTodaysHabits()
        if (todaysHabits.isNotEmpty() && todaysHabits.any { it.isCompletedToday() }) {
            streak = (1..7).random() // Mock streak between 1-7 days
        }

        return streak
    }

    private fun navigateToAddHabit() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, AddFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToMoodJournal() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MoodFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToHydration() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HydrationSettingsFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToSettings() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SettingsFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToHabitsList() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, TodoFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToHabitDetails(habit: com.royce.habitify.models.Habit) {
        // TODO: Navigate to habit details/edit screen
        android.widget.Toast.makeText(context, "Habit: ${habit.title}", android.widget.Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        // Refresh data when returning to this fragment
        updateDashboard()
        loadTodaysHabits()
    }
}
