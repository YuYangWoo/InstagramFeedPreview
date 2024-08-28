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
    implementation(libs.paging.common)

    implementation(project(":data:datasource"))
    implementation(project(":data:models"))
    implementation(project(":domain:model"))
}
