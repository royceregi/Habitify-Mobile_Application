package com.royce.habitify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.royce.habitify.adapter.HabitAdapter
import com.royce.habitify.data.DataManager
import com.royce.habitify.models.Habit
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class TodoFragment : Fragment() {

    private lateinit var dataManager: DataManager
    private lateinit var habitAdapter: HabitAdapter
    private lateinit var rvHabits: RecyclerView
    private lateinit var tvCurrentDate: TextView
    private lateinit var tvCompletedCount: TextView
    private lateinit var tvCompletionPercentage: TextView
    private lateinit var tvTotalHabits: TextView
    private lateinit var tvStreakCount: TextView
    private lateinit var progressBar: com.google.android.material.progressindicator.LinearProgressIndicator
    private lateinit var emptyState: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataManager = DataManager.getInstance(requireContext())

        initializeViews(view)
        setupRecyclerView()
        setupClickListeners()
        loadHabits()
        updateUI()
    }

    private fun initializeViews(view: View) {
        rvHabits = view.findViewById(R.id.rv_habits)
        tvCurrentDate = view.findViewById(R.id.tv_current_date)
        tvCompletedCount = view.findViewById(R.id.tv_completed_count)
        tvCompletionPercentage = view.findViewById(R.id.tv_completion_percentage)
        tvTotalHabits = view.findViewById(R.id.tv_total_habits)
        tvStreakCount = view.findViewById(R.id.tv_streak_count)
        progressBar = view.findViewById(R.id.progress_bar)
        emptyState = view.findViewById(R.id.empty_state)
    }

    private fun setupRecyclerView() {
        habitAdapter = HabitAdapter(
            onHabitClick = { habit ->
                // Navigate to habit details/edit (will implement later)
                showHabitDetails(habit)
            },
            onHabitToggle = { habit ->
                toggleHabitCompletion(habit)
            }
        )

        rvHabits.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = habitAdapter
        }
    }

    private fun setupClickListeners() {
        // Empty state click to navigate to add habit
        emptyState.setOnClickListener {
            navigateToAddHabit()
        }
    }

    private fun loadHabits() {
        val habits = dataManager.getTodaysHabits()
        habitAdapter.submitList(habits)
        updateEmptyStateVisibility(habits.isEmpty())
    }

    private fun updateUI() {
        // Update current date
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd")
        tvCurrentDate.text = today.format(formatter)

        // Update progress
        val completionPercentage = dataManager.getTodaysCompletionPercentage()
        val todaysHabits = dataManager.getTodaysHabits()
        val completedCount = todaysHabits.count { it.isCompletedToday() }

        tvCompletedCount.text = completedCount.toString()
        tvTotalHabits.text = todaysHabits.size.toString()
        tvCompletionPercentage.text = "$completionPercentage%"
        progressBar.progress = completionPercentage

        // Calculate and display streak
        val currentStreak = calculateCurrentStreak()
        tvStreakCount.text = currentStreak.toString()
    }

    private fun toggleHabitCompletion(habit: Habit) {
        dataManager.toggleHabitCompletion(habit.id)
        loadHabits() // Refresh the list
        updateUI() // Update progress summary
    }

    private fun showHabitDetails(habit: Habit) {
        // For now, just show a simple toast. Will implement full details later
        android.widget.Toast.makeText(
            context,
            "Clicked: ${habit.title}",
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }

    private fun navigateToAddHabit() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, AddFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun updateEmptyStateVisibility(isEmpty: Boolean) {
        if (isEmpty) {
            rvHabits.visibility = View.GONE
            emptyState.visibility = View.VISIBLE
        } else {
            rvHabits.visibility = View.VISIBLE
            emptyState.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh data when returning to this fragment
        loadHabits()
        updateUI()
    }

    private fun calculateCurrentStreak(): Int {
        // Simple implementation - count consecutive completed days
        // For now, return a mock streak based on habit completion
        val habits = dataManager.getHabits()
        if (habits.isEmpty()) return 0
        
        // Calculate streak based on most recent completions
        val today = org.threeten.bp.LocalDate.now()
        var streak = 0
        
        // Check last 30 days for consecutive completions
        for (i in 0..29) {
            val checkDate = today.minusDays(i.toLong())
            val completedHabitsOnDate = habits.count { habit ->
                habit.completedDates.contains(checkDate)
            }
            
            if (completedHabitsOnDate > 0) {
                streak++
            } else {
                break
            }
        }
        
        return streak
    }
}
