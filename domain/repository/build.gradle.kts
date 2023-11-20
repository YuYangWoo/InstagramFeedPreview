plugins {
    id ("instagram.kotlin")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.java.inject)
    implementation(libs.paging.common)

    implementation(project(":domain:model"))
}
