plugins {
    id ("instagram.android")
    id ("instagram.hilt")
    id ("instagram.test")
}

android {
    namespace = "com.example.board"
}

dependencies {
    implementation(libs.glide)
    implementation(libs.swipeRefreshLayout)
    testImplementation(libs.bundles.kotest)
    testImplementation(libs.mockk)
    implementation(project(":domain:usecase"))
    implementation(project(":domain:model"))
}
