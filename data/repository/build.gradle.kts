plugins {
    id ("instagram.kotlin")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.java.inject)

    implementation(project(":core:dto"))
    implementation(project(":domain:repository"))
    implementation(project(":domain:model"))
    implementation(project(":data:datasource"))
}
