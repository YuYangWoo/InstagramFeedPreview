plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("androidx.navigation.safeargs.kotlin")
    id ("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.board"
    compileSdk = 33

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
    buildFeatures {
        viewBinding = true
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
    implementation(libs.glide)
    implementation(libs.swipeRefreshLayout)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(project(":domain:usecase"))
    implementation(project(":domain:model"))
}
