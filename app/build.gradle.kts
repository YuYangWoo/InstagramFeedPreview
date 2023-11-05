plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("androidx.navigation.safeargs.kotlin")
    id ("com.google.dagger.hilt.android")
    id ("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

kotlin {
    jvmToolchain(17)
}

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "com.example.instagramfeedpreview"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    namespace = "com.example.instagramfeedpreview"
}

dependencies {
    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ktx)
    kapt(libs.hilt.compiler)

    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(project(":domain:usecase"))
    implementation(project(":data:repository"))
    implementation(project(":data:datasource"))
    implementation(project(":domain:repository"))
    implementation(project(":feature:main"))
    implementation(project(":feature:login"))
    implementation(project(":feature:board"))
}
