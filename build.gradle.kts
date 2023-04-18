buildscript {
    val kotlin_version by extra("1.8.20")
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.android.library) apply false
    id("org.jetbrains.kotlin.android") version "1.8.20" apply false
}

allprojects {
    repositories {
        maven { url = uri("https://jitpack.io") }
        google()
        mavenCentral()
    }
}
