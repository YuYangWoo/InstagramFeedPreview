plugins {
    id ("instagram.feature")
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
