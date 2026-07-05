plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.0.21"
    id("com.google.gms.google-services")
}

android {
    namespace = "com.yaeldev.cursitodefundamentosandroid"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.yaeldev.cursitodefundamentosandroid"
        minSdk = 28
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
        // firebase-auth (BOM 34.x) trae metadata de Kotlin 2.3 y el proyecto usa 2.0.21;
        // solo consumimos su API Java, asi que ignoramos el chequeo de version de metadata.
        freeCompilerArgs += "-Xskip-metadata-version-check"
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    val nav_version = "2.9.8"
    // Jetpack Compose integration
    implementation("androidx.navigation:navigation-compose:${nav_version}")
    // JSON serialization: la usan las rutas type-safe de Navigation Compose
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.15.0"))
    // Cloud Firestore (fuente de verdad del CRUD de contactos)
    implementation("com.google.firebase:firebase-firestore")
    // Firebase Authentication (login/registro con correo y contraseña)
    implementation("com.google.firebase:firebase-auth")
    // await() sobre las Task de Play Services desde corrutinas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.9.0")
}