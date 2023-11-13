plugins {
    id ("instagram.kotlin")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    implementation(project(":domain:model"))
    implementation(project(":data:dto"))
}