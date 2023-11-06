plugins {
    id ("instagram.feature")
}

android {
    namespace = "com.example.main"
}

dependencies {
    implementation(project(":domain:usecase"))

    testImplementation(libs.bundles.kotest)
    testImplementation(libs.mockk)
}
