plugins {
    id ("instagram.kotlin")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    implementation(project(":data:dto"))
}