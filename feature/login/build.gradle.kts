plugins {
    id ("instagram.android")
    id ("kotlin-kapt")
    id ("instagram.hilt")
}

android {
    namespace = "com.example.login"
}

dependencies {
    testImplementation(libs.bundles.kotest)
    testImplementation(libs.mockk)

    implementation(project(":domain:usecase"))
    implementation(project(":domain:model"))
}
