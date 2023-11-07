plugins {
    id ("instagram.android")
    id ("instagram.hilt")
}

android {
    namespace = "com.example.datastore"
}

dependencies {
    implementation(libs.androidx.dataStore.preferences)
    implementation(project(":data:datasource"))
}
