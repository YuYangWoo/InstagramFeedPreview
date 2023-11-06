plugins {
    id ("instagram.kotlin")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.java.inject)

    implementation(project(":domain:repository"))
    implementation(project(":domain:model"))
    implementation(project(":data:dto"))
    implementation(project(":data:datasource"))
}
