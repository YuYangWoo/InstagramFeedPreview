plugins {
    id ("instagram.kotlin")
}

dependencies {
    implementation(libs.java.inject)
    implementation(libs.kotlinx.coroutines.core)

    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(project(":core:dto"))
    implementation(project(":domain:repository"))
    implementation(project(":domain:model"))
    implementation(project(":data:datasource"))
}
