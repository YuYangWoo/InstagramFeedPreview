plugins {
    id ("instagram.android")
    id ("instagram.hilt")
}

android {
    namespace = "com.example.datastore"
}

dependencies {
    implementation(libs.androidx.dataStore.preferences)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(project(":data:datasource"))
}
