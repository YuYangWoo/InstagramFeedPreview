plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("androidx.navigation.safeargs.kotlin")
    id ("com.google.dagger.hilt.android")
    id ("kotlin-parcelize")
    id("com.google.devtools.ksp")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ktx)
    testImplementation(libs.junit4)
    androidTestImplementation (libs.android.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    // NavigationActivity & BaseView
    implementation(libs.yyw.android.library)

    // Glide
    implementation(libs.glide)

    // Coroutine
    implementation(libs.bundles.coroutine)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewModel.ktx)

    // Retrofit
    implementation(libs.bundles.retrofit)
    // Room
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)
    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // SwipeRefreshLayout
    implementation(libs.swipeRefreshLayout)

    // DataStore
    implementation(libs.androidx.dataStore.preferences)
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(project(":domain:usecase"))
    implementation(project(":data:repository"))
    implementation(project(":domain:repository"))
}
