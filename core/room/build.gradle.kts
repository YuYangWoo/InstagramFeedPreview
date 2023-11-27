@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id ("instagram.android")
    id ("instagram.hilt")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.room"
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }
    }
}

dependencies {
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)
    implementation(libs.room.paging)
    implementation(libs.retrofit.gson)
    implementation(libs.paging.common)

    implementation(project(":data:datasource"))
    implementation(project(":domain:model"))
}