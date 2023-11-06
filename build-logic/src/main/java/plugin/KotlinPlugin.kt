package plugin

import Build
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.getByType

internal class KotlinPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        applyPlugin(target.pluginManager)
        applyJavaPluginExtension(target.extensions.getByType())
    }

    private fun applyPlugin(pluginManager: PluginManager) = pluginManager.apply {
        apply("org.jetbrains.kotlin.jvm")
    }

    private fun applyJavaPluginExtension(extension: JavaPluginExtension) = extension.apply {
        sourceCompatibility = Build.SOURCE_COMPATIBILITY
        targetCompatibility = Build.TARGET_COMPATIBILITY
    }

}