@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id ("instagram.android")
    id ("instagram.hilt")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.datastore"
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
    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)
}