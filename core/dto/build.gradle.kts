plugins {
    id ("instagram.android")
}

android {
    namespace = "com.example.dto"
}

dependencies {
    implementation(libs.retrofit.gson)

    implementation(project(":domain:model"))
}