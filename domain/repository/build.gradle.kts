plugins {
    id ("instagram.kotlin")
    id ("kotlin-kapt")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.java.inject)

    implementation(project(":domain:model"))
}
