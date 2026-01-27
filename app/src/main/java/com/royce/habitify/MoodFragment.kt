package com.royce.habitify

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.royce.habitify.adapter.MoodAdapter
import com.royce.habitify.data.DataManager
import com.royce.habitify.models.MoodEntry
import org.threeten.bp.LocalDateTime
import java.util.*

class MoodFragment : Fragment() {

    private lateinit var dataManager: DataManager
    private lateinit var selectedEmoji: String
    private lateinit var etMoodNotes: TextInputEditText
    private lateinit var btnSaveMood: MaterialButton
    private lateinit var rvMoodHistory: RecyclerView
    private lateinit var emptyMoodState: LinearLayout
    private lateinit var moodAdapter: MoodAdapter

    private val emojiButtons = mutableMapOf<String, TextView>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mood, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize DataManager
        dataManager = DataManager.getInstance(requireContext())

        // Initialize UI components
        initializeViews(view)
        setupEmojiPicker()
        setupRecyclerView()
        setupSaveButton()
        loadMoodHistory()
    }

    private fun initializeViews(view: View) {
        etMoodNotes = view.findViewById(R.id.et_mood_notes)
        btnSaveMood = view.findViewById(R.id.btn_save_mood)
        rvMoodHistory = view.findViewById(R.id.rv_mood_history)
        emptyMoodState = view.findViewById(R.id.empty_mood_state)

        // Initialize with default emoji
        selectedEmoji = "😊"
    }

    private fun setupEmojiPicker() {
        // Map of emoji IDs to their values
        val emojiMap = mapOf(
            "emoji_happy" to "😊",
            "emoji_very_happy" to "😄",
            "emoji_love" to "🥰",
            "emoji_excited" to "🤩",
            "emoji_proud" to "😎",
            "emoji_neutral" to "😐",
            "emoji_sad" to "😢",
            "emoji_angry" to "😠",
            "emoji_tired" to "😴",
            "emoji_thinking" to "🤔"
        )

        // Set up click listeners for each emoji
        emojiMap.forEach { (id, emoji) ->
            val emojiButton = requireView().findViewById<TextView>(resources.getIdentifier(id, "id", requireContext().packageName))
            emojiButtons[id] = emojiButton
            
            emojiButton.setOnClickListener {
                selectEmoji(emoji, emojiButton)
            }
        }

        // Select default emoji
        selectEmoji("😊", emojiButtons["emoji_happy"]!!)
    }

    private fun selectEmoji(emoji: String, button: TextView) {
        selectedEmoji = emoji
        
        // Reset all buttons
        emojiButtons.values.forEach { btn ->
            btn.alpha = 0.5f
            btn.background = null
        }
        
        // Highlight selected button
        button.alpha = 1.0f
        button.setBackgroundResource(R.drawable.bg_circle_teal)
    }

    private fun setupRecyclerView() {
        moodAdapter = MoodAdapter { moodEntry ->
            shareMoodEntry(moodEntry)
        }
        
        rvMoodHistory.layoutManager = LinearLayoutManager(requireContext())
        rvMoodHistory.adapter = moodAdapter
    }

    private fun setupSaveButton() {
        btnSaveMood.setOnClickListener {
            saveMoodEntry()
        }
    }

    private fun saveMoodEntry() {
        val notes = etMoodNotes.text?.toString()?.trim() ?: ""
        
        val moodEntry = MoodEntry(
            id = UUID.randomUUID().toString(),
            emoji = selectedEmoji,
            moodText = "", // We're using emoji instead of text
            dateTime = LocalDateTime.now(),
            notes = notes
        )

        try {
            // Save mood entry
            val existingEntries = dataManager.getMoodEntries().toMutableList()
            existingEntries.add(moodEntry)
            dataManager.saveMoodEntries(existingEntries)

            // Show success message
            Toast.makeText(requireContext(), "Mood saved successfully! 😊", Toast.LENGTH_SHORT).show()

            // Clear form
            etMoodNotes.text?.clear()
            selectEmoji("😊", emojiButtons["emoji_happy"]!!)

            // Refresh mood history
            loadMoodHistory()

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Failed to save mood: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadMoodHistory() {
        val moodEntries = dataManager.getMoodEntries()
        moodAdapter.submitList(moodEntries)

        // Show/hide empty state
        if (moodEntries.isEmpty()) {
            rvMoodHistory.visibility = View.GONE
            emptyMoodState.visibility = View.VISIBLE
        } else {
            rvMoodHistory.visibility = View.VISIBLE
            emptyMoodState.visibility = View.GONE
        }
    }

    private fun shareMoodEntry(moodEntry: MoodEntry) {
        val shareText = buildString {
            append("My mood today: ${moodEntry.emoji} ${moodEntry.getMoodCategory()}\n")
            append("Time: ${moodEntry.getFormattedTime()}\n")
            if (moodEntry.notes.isNotEmpty()) {
                append("Notes: ${moodEntry.notes}\n")
            }
            append("Tracked with Wellness App 📱")
        }

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }

        startActivity(Intent.createChooser(shareIntent, "Share your mood"))
    }

    override fun onResume() {
        super.onResume()
        loadMoodHistory()
    }
}
