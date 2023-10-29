plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
}

android {
    namespace = "com.example.network"
    compileSdk = 33
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.bundles.retrofit)
    testImplementation(libs.junit4)
    androidTestImplementation(libs.android.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    implementation(libs.okhttp.logging)
    implementation(project(":data:datasource"))
    implementation(project(":core:dto"))
}
