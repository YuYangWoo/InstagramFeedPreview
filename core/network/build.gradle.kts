plugins {
    id ("instagram.android")
    id ("instagram.hilt")
}

android {
    namespace = "com.example.network"
}

dependencies {
    implementation(libs.bundles.retrofit)
    implementation(libs.okhttp.logging)

    implementation(project(":data:datasource"))
    implementation(project(":core:dto"))
}
