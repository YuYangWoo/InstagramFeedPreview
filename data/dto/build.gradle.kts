plugins {
    id ("instagram.kotlin")
}

dependencies {
    implementation(libs.retrofit.gson)

    implementation(project(":domain:model"))
}