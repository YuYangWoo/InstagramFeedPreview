import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id ("instagram.feature")
}


android {
    namespace = "com.example.login"

    defaultConfig {
        buildConfigField("String", "CLIENT_ID", getApiKey("CLIENT_ID"))
        buildConfigField("String", "CLIENT_SECRET", getApiKey("CLIENT_SECRET"))
    }
}

dependencies {
    testImplementation(libs.bundles.kotest)
    testImplementation(libs.mockk)

    implementation(project(":domain:usecase"))
    implementation(project(":domain:model"))
}

fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}