plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.royce.habitify"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.royce.habitify"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    // AndroidX AppCompat
    implementation("androidx.appcompat:appcompat:1.7.0")
    // Material Components (for BottomNavigationView, themes, etc.)
    implementation("com.google.android.material:material:1.12.0")
    // AndroidX ConstraintLayout
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    // Fragment KTX (for easier fragment transactions if needed)
    implementation("androidx.fragment:fragment-ktx:1.8.5")
    // Gson for JSON serialization (required for SharedPreferences data storage)
    implementation("com.google.code.gson:gson:2.10.1")
    // ThreeTenABP for java.time support on older Android versions
    implementation("com.jakewharton.threetenabp:threetenabp:1.4.6")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}