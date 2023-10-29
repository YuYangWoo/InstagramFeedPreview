plugins {
    id ("instagram.android")
    id ("kotlin-kapt")
    id ("instagram.hilt")
    id ("instagram.test")
}

android {
    namespace = "com.example.main"
}

dependencies {
    implementation(project(":domain:usecase"))

    testImplementation(libs.bundles.kotest)
    testImplementation(libs.mockk)
}
