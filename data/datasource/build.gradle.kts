plugins {
    id ("instagram.android")
}
android {
    namespace = "com.example.datasource"
}
dependencies {
    implementation(project(":core:dto"))
}