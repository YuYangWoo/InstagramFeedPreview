package plugin

import com.android.build.api.dsl.LibraryDefaultConfig
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.getByType

internal class AndroidLibrary : Plugin<Project> {
    private val androidPlugin = AndroidPlugin()

    override fun apply(target: Project) {
        applyPlugin(target.pluginManager)
        applyAndroidExtension(target.extensions.getByType())
        androidPlugin.apply(target)
    }

    private fun applyPlugin(pluginManager: PluginManager) = pluginManager.apply {
        apply("com.android.library")
    }

    private fun applyAndroidExtension(extension: LibraryExtension) = with(extension) {
        applyDefaultConfig(defaultConfig)
    }

    private fun applyDefaultConfig(config: LibraryDefaultConfig) = with(config) {
        consumerProguardFiles("consumer-rules.pro")
    }

}