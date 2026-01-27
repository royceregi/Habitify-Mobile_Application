# 🌱 Habitify - Wellness & Habit Tracker

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green.svg" alt="Platform">
  <img src="https://img.shields.io/badge/Language-Kotlin-blue.svg" alt="Language">
  <img src="https://img.shields.io/badge/License-Unspecified-lightgrey.svg" alt="License">
</p>

A modern, intuitive Android application for tracking daily habits, journaling moods, managing hydration, and supporting positive lifestyle changes.

## ✨ Features

### 🏡 Dashboard
- Daily overview of tracked habits, hydration, and mood states
- Quick stats and ongoing streaks
- Fast access to core app sections

### 🏆 Habit Tracking
- Add new habits to cultivate
- Daily, weekly, or custom scheduling
- Track completion streaks and receive reminders
- Comprehensive list of habits with edit/delete functionality

### 💧 Hydration Monitor
- Log glasses of water consumed each day
- Set hydration targets & get reminder notifications
- Hydration tips to motivate healthy routines

### 🌈 Mood Journal
- Record mood entries with notes
- Visualize mood patterns over time
- Privacy-first: mood data is stored locally

### 🔔 Smart Notifications
- Habit and hydration reminders that you can toggle on/off
- Customizable notification times for daily check-ins

### ⚙️ Settings & Account Management
- Edit and manage your profile
- Theme switching (Light/Dark mode)
- Export and clear data (feature roadmap)
- About & support section with contact links

## 🛠️ Technical Stack

### Core Technologies
- **Language**: Kotlin 100%
- **Min SDK**: Android 7.0 (API 24)
- **Target SDK**: Android 15 (API 35)
- **Build System**: Gradle (Kotlin DSL)

### Key Libraries & Frameworks
- **Material Design 3**: Modern, accessible UI components
- **AndroidX Core & AppCompat**
- **Fragments & ViewModel**: Clean architecture patterns
- **RecyclerView**: List and streaks display
- **ConstraintLayout**: Responsive layouts

### Architecture
- **Pattern**: Fragment-based navigation with multiple sections (Habits, Hydration, Mood, Settings)
- **Data Models**: Simple Kotlin data classes (Habit, MoodEntry, HydrationLog)
- **UI**: XML layouts (with support for Compose in future roadmap)
- **Adapters**: For list and history views

## 📱 App Structure

```
Habitify-Mobile_Application/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/royce/habitify/
│   │   │   │   ├── model/                # Data models
│   │   │   │   ├── ui/                   # Fragments & activities
│   │   │   │   ├── adapter/              # RecyclerView adapters
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── SettingsFragment.kt
│   │   │   │   ├── HabitFragment.kt
│   │   │   │   ├── HydrationFragment.kt
│   │   │   │   └── MoodFragment.kt
│   │   │   └── res/                      # Resources (layouts, drawables, etc.)
│   │   ├── androidTest/                  # Instrumentation tests
│   │   └── test/                         # Unit tests
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── DESIGN_SYSTEM_GUIDE.md
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 35
- Kotlin 1.9+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/RoyceAbiel426/Habitify-Mobile_Application.git
   cd Habitify-Mobile_Application
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory

3. **Sync Gradle**
   - Android Studio will automatically sync Gradle files
   - Wait for dependencies to download

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click the "Run" button (▶️) or press `Shift + F10`

## 🛒 Key Components

### MainActivity
The main container activity with fragment navigation for:
- Habits
- Hydration
- Mood Journal
- Settings

### Data Models

**Habit**
```kotlin
data class Habit(
    val title: String,
    val frequency: String,
    val isCompletedToday: Boolean
)
```

**MoodEntry**
```kotlin
data class MoodEntry(
    val date: String,
    val mood: String,
    val notes: String?
)
```

**HydrationLog**
```kotlin
data class HydrationLog(
    val date: String,
    val glasses: Int
)
```

## 🎨 Design Highlights

- **Material Design 3** principles for a modern look
- Simple, focused screens for habit, hydration, and mood
- Widget for wellness stats (coming soon)
- Card-based organization for clarity
- Support for both light and dark themes

## 🔐 User Flow

1. **Get Started**: Launch app for welcome screen/tour (future)
2. **Dashboard**: View habit, hydration, and mood status
3. **Manage Habits**: Add/track new habits & streaks
4. **Mood Journal**: Record and review moods
5. **Hydration Tracker**: Log water intake, set reminders
6. **Settings**: Profile, app preferences, support

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

### Instrumentation Tests
```bash
./gradlew connectedAndroidTest
```

## 📦 Dependencies

Key dependencies include:
- `androidx.core:core-ktx:1.17.0`
- `com.google.android.material:material:1.12.0`
- `androidx.recyclerview:recyclerview:1.3.2`

See [build.gradle.kts](app/build.gradle.kts) for the complete list.

## 🛣️ Roadmap

- [ ] Data backup and export features
- [ ] Widget for quick-view of stats
- [ ] Google & social sign-in
- [ ] Mood and streak analytics
- [ ] Cloud sync
- [ ] More customization of reminder times
- [ ] Biometric authentication support

## 👤 Author

**Royce Abiel**
- GitHub: [@RoyceAbiel426](https://github.com/RoyceAbiel426)

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! Feel free to check the [issues page](https://github.com/RoyceAbiel426/Habitify-Mobile_Application/issues).

## ⭐ Show Your Support

Give a ⭐️ if this project helped you!

---

<p align="center">Made with ❤️ and Kotlin</p>