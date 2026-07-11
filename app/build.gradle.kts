import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.0.21"
    id("com.google.gms.google-services")
}

//val keystoreProps = Properties().apply {
//    val f = rootProject.file("keystore.properties")
//    if(f.exists()) load(f.inputStream())
//}

android {
    namespace = "com.yaeldev.cursitodefundamentosandroid"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.yaeldev.cursitodefundamentosandroid"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

//    signingConfigs {
//        create("release") {
//            storeFile = file(keystoreProps.getProperty("storeFile"))
//            storePassword = keystoreProps.getProperty("storePassword")
//            keyAlias = keystoreProps.getProperty("keyAlias")
//            keyPassword = keystoreProps.getProperty("keyPassword")
//        }
//    }

    buildTypes {
        //Investigar keystore para variables de entorno y secrets
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
            isMinifyEnabled = false
            isDebuggable = true
            resValue("string","app_name", "Cursito Debug")
            buildConfigField("String","API_URL","\"AQUI_DEBERIA_IR_TU_BASE_URL_DEV_Y_NO_EN_PATHS\"") //example
        }
        create("qa"){
            initWith(getByName("release"))
            applicationIdSuffix = ".qa"
            versionNameSuffix = "-QA"
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = false
            resValue("string","app_name", "Cursito QA")
            buildConfigField("String","API_URL","\"AQUI_DEBERIA_IR_TU_BASE_URL_QA_Y_NO_EN_PATHS\"") //example
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            //signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue("string","app_name", "Cursito")
            buildConfigField("String","API_URL","\"AQUI_DEBERIA_IR_TU_BASE_URL_Y_NO_EN_PATHS\"") //example
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
        buildConfig = true
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
    // Firebase Cloud Messaging (push; el envío lo dispara un Cloudflare Worker)
    implementation("com.google.firebase:firebase-messaging")
    // await() sobre las Task de Play Services desde corrutinas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.9.0")
    // Retrofit para el POST al Worker de push (converter kotlinx-serialization,
    // reutiliza kotlinx-serialization-json; no agrega Gson)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:2.11.0")
    // okhttp 4.x: aporta la extension toMediaType() que usa el converter
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}