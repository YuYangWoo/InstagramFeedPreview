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

    defaultConfig {
        minSdk = 14
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
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
    implementation(libs.glide)
    implementation(libs.swipeRefreshLayout)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(project(":domain:usecase"))
    implementation(project(":domain:model"))
    testImplementation(libs.bundles.kotest)
    testImplementation(libs.mockk)

}
