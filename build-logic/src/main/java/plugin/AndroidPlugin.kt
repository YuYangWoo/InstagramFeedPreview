package plugin

import com.android.build.api.dsl.CommonExtension
import ext.getVersionCatalog
import ext.implementation
import ext.kotlinOptions
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.getByType

internal class AndroidPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        applyPlugin(target.pluginManager)
        applyAndroidExtensions(target.extensions.getByType(CommonExtension::class))
        applyDependency(target.dependencies, target.getVersionCatalog())
    }

    private fun applyPlugin(pluginManager: PluginManager) = pluginManager.apply {
        apply("org.jetbrains.kotlin.android")
        apply("com.android.library")
        apply("androidx.navigation.safeargs.kotlin")
    }

    private fun applyAndroidExtensions(extensions: CommonExtension<*, *, *, *, *>)
    = extensions.apply {
        compileSdk = Build.COMPILE_SDK

        testOptions {
            unitTests.all {
                it.useJUnitPlatform()
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        kotlinOptions { jvmTarget = "17" }
        buildFeatures { viewBinding = true}
    }

    private fun applyDependency(dependencyHandler: DependencyHandler, libs: VersionCatalog)
    = dependencyHandler.apply {
        implementation(libs.findLibrary("androidx-core-ktx").get())
        implementation(libs.findLibrary("androidx-appcompat").get())
        implementation(libs.findLibrary("android-material").get())
        implementation(libs.findLibrary("androidx-constraintLayout").get())
        implementation(libs.findLibrary("androidx-navigation-fragment").get())
        implementation(libs.findLibrary("androidx-navigation-ktx").get())
    }

}