package com.royce.habitify.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.royce.habitify.R
import com.royce.habitify.models.MoodEntry

class MoodAdapter(
    private val onMoodShare: (MoodEntry) -> Unit
) : BaseAdapter<MoodEntry>(R.layout.item_mood_entry) {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val moodEntry = getItem(position) ?: return
        (holder as MoodViewHolder).bind(moodEntry)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mood_entry, parent, false)
        return MoodViewHolder(view, onMoodShare)
    }

    inner class MoodViewHolder(
        itemView: View,
        private val onMoodShare: (MoodEntry) -> Unit
    ) : BaseViewHolder(itemView) {

        private val tvMoodEmoji: TextView = itemView.findViewById(R.id.tv_mood_emoji)
        private val tvMoodCategory: TextView = itemView.findViewById(R.id.tv_mood_category)
        private val tvMoodTime: TextView = itemView.findViewById(R.id.tv_mood_time)
        private val tvMoodDate: TextView = itemView.findViewById(R.id.tv_mood_date)
        private val tvMoodNotes: TextView = itemView.findViewById(R.id.tv_mood_notes)
        private val ivShareMood: ImageView = itemView.findViewById(R.id.iv_share_mood)

        init {
            ivShareMood.setOnClickListener {
                val position = adapterPosition
                if (position != androidx.recyclerview.widget.RecyclerView.NO_POSITION) {
                    getItem(position)?.let { moodEntry ->
                        onMoodShare(moodEntry)
                    }
                }
            }
        }

        fun bind(moodEntry: MoodEntry) {
            tvMoodEmoji.text = moodEntry.emoji
            tvMoodCategory.text = moodEntry.getMoodCategory()
            tvMoodTime.text = moodEntry.getFormattedTime()
            tvMoodDate.text = moodEntry.getFormattedDate()

            // Show notes if available
            if (moodEntry.notes.isNotEmpty()) {
                tvMoodNotes.text = moodEntry.notes
                tvMoodNotes.visibility = View.VISIBLE
            } else {
                tvMoodNotes.visibility = View.GONE
            }

            // Set background color based on mood category
            val backgroundColor = when (moodEntry.getMoodCategory().lowercase()) {
                "happy" -> R.color.success_light
                "excited" -> R.color.success_light
                "loving" -> R.color.accent_purple
                "neutral" -> R.color.text_hint
                "sad" -> R.color.info_light
                "angry" -> R.color.error_light
                "tired" -> R.color.warning_light
                "thoughtful" -> R.color.secondary_calm_blue
                else -> R.color.habit_health
            }
            
            tvMoodEmoji.setBackgroundResource(backgroundColor)
        }
    }
}
