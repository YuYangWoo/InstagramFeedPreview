plugins {
    `kotlin-dsl`
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
        register("instagram.feature") {
            id = "instagram.feature"
            implementationClass = "plugin.FeaturePlugin"
        }
        register("instagram.application") {
            id = "instagram.application"
            implementationClass = "plugin.ApplicationPlugin"
        }
    }
}
