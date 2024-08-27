plugins {
    id ("instagram.feature")
}

android {
    namespace = "com.example.main"
}

dependencies {
    implementation(project(":domain:usecase"))
    implementation(project(":core:testing"))

    testImplementation(libs.bundles.kotest)
    testImplementation(libs.mockk)
}
