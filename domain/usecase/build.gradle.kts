plugins {
    id ("instagram.kotlin")
}

dependencies {
    implementation(libs.java.inject)
    implementation(libs.kotlinx.coroutines.core)

    implementation(project(":domain:repository"))
    implementation(project(":domain:model"))
}
