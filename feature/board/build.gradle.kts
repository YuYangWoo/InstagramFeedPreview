plugins {
    id ("instagram.feature")
}

android {
    namespace = "com.example.board"
}

dependencies {
    implementation(libs.glide)
    implementation(libs.swipeRefreshLayout)
    testImplementation(libs.bundles.kotest)
    testImplementation(libs.mockk)
    implementation(libs.paging.runtime)
    implementation(libs.androidx.activity)

    implementation(project(":domain:usecase"))
    implementation(project(":domain:model"))
}
