plugins {
    id("com.android.application") // 👈 sin versión aquí
    id("org.jetbrains.kotlin.android") // 👈 sin versión
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
}

android {
    namespace = "com.example.pitstop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pitstop"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.6.0"
    }
}

dependencies {
    // --- Jetpack Compose base ---
    implementation(platform("androidx.compose:compose-bom:2024.09.02"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    debugImplementation("androidx.compose.ui:ui-tooling")

    // --- Navegación con Compose ---
    implementation("androidx.navigation:navigation-compose:2.8.0")

    // --- Activity Compose ---
    implementation("androidx.activity:activity-compose:1.9.2")

    // --- Para LocalDateTime en API baja ---
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
}
