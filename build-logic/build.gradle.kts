@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(17)
    explicitApi()
}

dependencies {
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.android.gradlePlugin)
    implementation(libs.hilt.gradle)
}

gradlePlugin {
    plugins {
        register("instagram.hilt") {
            id = "instagram.hilt"
            implementationClass = "plugin.HiltPlugin"
        }
        register("instagram.android") {
            id = "instagram.android"
            implementationClass = "plugin.AndroidPlugin"
        }
        register("instagram.test") {
            id = "instagram.test"
            implementationClass = "plugin.TestPlugin"
        }
        register("instagram.kotlin") {
            id = "instagram.kotlin"
            implementationClass = "plugin.KotlinPlugin"
        }
    }
}
