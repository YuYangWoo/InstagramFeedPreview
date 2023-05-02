import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")

}

android {
    namespace = "com.example.repository"
    compileSdk = 33

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

dependencies {
    implementation(project(":core:network"))
    implementation(libs.bundles.coroutine)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}
