plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
}

android {
    namespace = "com.example.network"
    compileSdk = 33

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

dependencies {
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.bundles.retrofit)
    testImplementation(libs.junit4)
    androidTestImplementation(libs.android.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    implementation(project(":data:dto"))
    implementation(project(":data:datasource"))
}
