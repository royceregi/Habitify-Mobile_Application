package com.royce.habitify.adapter

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.royce.habitify.R
import com.royce.habitify.models.Habit

class HabitAdapter(
    private val onHabitClick: (Habit) -> Unit,
    private val onHabitToggle: (Habit) -> Unit
) : BaseAdapter<Habit>(R.layout.item_progress_card) {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val habit = getItem(position) ?: return
        (holder as HabitViewHolder).bind(habit)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): BaseViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_progress_card, parent, false)
        return HabitViewHolder(view, onHabitClick, onHabitToggle)
    }

    inner class HabitViewHolder(
        itemView: View,
        private val onHabitClick: (Habit) -> Unit,
        private val onHabitToggle: (Habit) -> Unit
    ) : BaseViewHolder(itemView) {

        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_icon)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvSubtitle: TextView = itemView.findViewById(R.id.tv_subtitle)
        private val tvProgress: TextView = itemView.findViewById(R.id.tv_progress)
        private val progressBar: LinearProgressIndicator = itemView.findViewById(R.id.progress_bar)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    getItem(position)?.let { habit ->
                        onHabitClick(habit)
                    }
                }
            }
        }

        fun bind(habit: Habit) {
            tvTitle.text = habit.title
            tvSubtitle.text = "${habit.period} • ${habit.habitType}"
            tvProgress.text = "${habit.getCompletionPercentage()}%"

            progressBar.progress = habit.getCompletionPercentage()

            // Set icon and background based on habit type
            val (iconRes, backgroundColor) = when (habit.habitType.lowercase()) {
                "health" -> R.drawable.ic_add to R.color.habit_health
                "study" -> R.drawable.ic_calendar to R.color.habit_study
                "work" -> R.drawable.ic_settings to R.color.habit_work
                "leisure" -> R.drawable.ic_home to R.color.habit_leisure
                else -> R.drawable.ic_add to R.color.habit_health
            }
            ivIcon.setImageResource(iconRes)

            // Set background color for the icon container (assuming it's in the layout)
            try {
                val iconContainer = itemView.findViewById<androidx.cardview.widget.CardView>(R.id.icon_container)
                iconContainer?.setCardBackgroundColor(itemView.context.getColor(backgroundColor))
            } catch (e: Exception) {
                // Icon container might not exist, ignore
            }

            // Note: Completion status is shown via progress indicator and text
        }

        override fun onItemClick(position: Int) {
            getItem(position)?.let { habit ->
                onHabitToggle(habit)
            }
        }
    }
}
