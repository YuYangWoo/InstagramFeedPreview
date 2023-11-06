package plugin

import Build
import com.android.build.api.dsl.CommonExtension
import ext.getVersionCatalog
import ext.implementation
import ext.kotlinOptions
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.getByType

class ApplicationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        applyPlugin(target.pluginManager)
        applyAndroidExtensions(target.extensions.getByType(CommonExtension::class))
        applyDependency(target.dependencies, target.getVersionCatalog())
    }

    private fun applyPlugin(pluginManager: PluginManager) = pluginManager.apply {
        apply("com.android.application")
        apply("org.jetbrains.kotlin.android")
        apply ("androidx.navigation.safeargs.kotlin")
    }

    private fun applyAndroidExtensions(extensions: CommonExtension<*, *, *, *, *>) = extensions.apply {
        compileSdk = Build.COMPILE_SDK

        defaultConfig {
            minSdk = Build.MIN_SDK
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            compileOptions {
                sourceCompatibility = Build.SOURCE_COMPATIBILITY
                targetCompatibility = Build.TARGET_COMPATIBILITY
            }

            kotlinOptions { jvmTarget = Build.JVM_TARGET }

            buildFeatures { viewBinding = true}
        }
    }

    private fun applyDependency(dependencyHandler: DependencyHandler, libs: VersionCatalog)
    = dependencyHandler.apply {
        implementation(libs.findLibrary("androidx-navigation-fragment").get())
        implementation(libs.findLibrary("androidx-navigation-ktx").get())
    }

}