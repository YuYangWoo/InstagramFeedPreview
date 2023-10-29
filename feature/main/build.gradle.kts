plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("instagram.hilt")
}

dependencies {
    implementation(project(":domain:usecase"))

    testImplementation(libs.bundles.kotest)
    testImplementation(libs.mockk)
}
