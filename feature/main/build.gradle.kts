plugins {
    id ("instagram.android")
    id ("kotlin-kapt")
    id ("instagram.hilt")
}

dependencies {
    implementation(project(":domain:usecase"))

    testImplementation(libs.bundles.kotest)
    testImplementation(libs.mockk)
}
