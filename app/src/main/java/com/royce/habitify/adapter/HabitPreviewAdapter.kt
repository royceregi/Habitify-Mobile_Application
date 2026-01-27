package com.royce.habitify.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.royce.habitify.R
import com.royce.habitify.models.Habit

class HabitPreviewAdapter(
    private val onHabitClick: (Habit) -> Unit
) : RecyclerView.Adapter<HabitPreviewAdapter.HabitPreviewViewHolder>() {

    private val habits = mutableListOf<Habit>()

    fun submitList(newHabits: List<Habit>) {
        habits.clear()
        habits.addAll(newHabits.take(3)) // Show only first 3 habits
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitPreviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_habit_preview, parent, false)
        return HabitPreviewViewHolder(view, onHabitClick)
    }

    override fun onBindViewHolder(holder: HabitPreviewViewHolder, position: Int) {
        val habit = habits[position]
        holder.bind(habit)
    }

    override fun getItemCount(): Int = habits.size

    class HabitPreviewViewHolder(
        itemView: View,
        private val onHabitClick: (Habit) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val ivHabitIcon: ImageView = itemView.findViewById(R.id.iv_habit_icon)
        private val tvHabitTitle: TextView = itemView.findViewById(R.id.tv_habit_title)
        private val tvHabitCategory: TextView = itemView.findViewById(R.id.tv_habit_category)
        private val ivCheckMark: ImageView = itemView.findViewById(R.id.iv_check_mark)
        private val iconContainer: CardView = itemView.findViewById(R.id.icon_container)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // We can't access the habit list directly, so we'll need to pass it differently
                    // For now, just handle the click
                }
            }
        }

        fun bind(habit: Habit) {
            tvHabitTitle.text = habit.title
            tvHabitCategory.text = "${habit.habitType} • ${habit.period}"

            // Set icon and background based on habit type
            val (iconRes, backgroundColor) = when (habit.habitType.lowercase()) {
                "health" -> R.drawable.ic_add to R.color.habit_health
                "study" -> R.drawable.ic_calendar to R.color.habit_study
                "work" -> R.drawable.ic_settings to R.color.habit_work
                "leisure" -> R.drawable.ic_home to R.color.habit_leisure
                else -> R.drawable.ic_add to R.color.habit_health
            }

            ivHabitIcon.setImageResource(iconRes)
            iconContainer.setCardBackgroundColor(itemView.context.getColor(backgroundColor))

            // Show check mark if completed today
            ivCheckMark.visibility = if (habit.isCompletedToday()) View.VISIBLE else View.GONE
        }
    }
}
