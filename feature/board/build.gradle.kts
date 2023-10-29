plugins {
    id ("instagram.android")
    id ("kotlin-kapt")
    id ("instagram.hilt")
    id ("instagram.test")
}

android {
    namespace = "com.example.board"
}

dependencies {
    implementation(libs.glide)
    implementation(libs.swipeRefreshLayout)
    implementation(project(":domain:usecase"))
    implementation(project(":domain:model"))
    testImplementation(libs.bundles.kotest)
    testImplementation(libs.mockk)

}
