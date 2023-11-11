plugins {
    id ("instagram.application")
    id ("instagram.hilt")
}

android {
    defaultConfig {
        applicationId = "com.example.instagramfeedpreview"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            isDebuggable = false
        }
        debug {
            isShrinkResources = false
            isMinifyEnabled = false

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            isDebuggable = true
        }
    }

    namespace = "com.example.instagramfeedpreview"
}

dependencies {
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(project(":domain:usecase"))
    implementation(project(":data:repository"))
    implementation(project(":data:datasource"))
    implementation(project(":domain:repository"))
    implementation(project(":feature:main"))
    implementation(project(":feature:login"))
    implementation(project(":feature:board"))
    implementation(project(":core:room"))
}
