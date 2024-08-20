package plugin

import com.android.build.api.dsl.CommonExtension
import ext.getVersionCatalog
import ext.implementation
import ext.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.getByType

internal class TestPlugin : Plugin<Project> {
    private val androidPlugin = AndroidPlugin()

    override fun apply(target: Project) {
        androidPlugin.apply(target)

        applyDependency(target.dependencies, target.getVersionCatalog())
    }

    private fun applyDependency(dependencyHandler: DependencyHandler, libs: VersionCatalog) = dependencyHandler.apply {
        implementation(libs.findLibrary("kotlin-reflect").get())
        testImplementation(libs.findBundle("kotest").get())
        testImplementation(libs.findLibrary("mockk").get())
        implementation(libs.findLibrary("kotlinx-coroutines-test").get())
        implementation(libs.findLibrary("androidx-test-rules").get())
    }

}