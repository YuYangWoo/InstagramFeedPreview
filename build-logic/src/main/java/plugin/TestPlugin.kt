package plugin

import com.android.build.api.dsl.CommonExtension
import ext.getVersionCatalog
import ext.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.getByType

internal class TestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        applyAndroidExtensions(target.extensions.getByType(CommonExtension::class))
        applyDependency(target.dependencies, target.getVersionCatalog())
    }

    private fun applyAndroidExtensions(extension: CommonExtension<*, *, *, *, *>)
    = extension.apply {
        testOptions {
            unitTests.all {
                it.useJUnitPlatform()
            }
        }
    }

    private fun applyDependency(dependencyHandler: DependencyHandler, libs: VersionCatalog) = dependencyHandler.apply {
        testImplementation(libs.findBundle("kotest").get())
        testImplementation(libs.findLibrary("mockk").get())
    }

}