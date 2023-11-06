package plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class FeaturePlugin : Plugin<Project> {
    private val androidPlugin = AndroidPlugin()
    private val hiltPlugin = HiltPlugin()
    private val testPlugin = TestPlugin()

    override fun apply(target: Project) {
        androidPlugin.apply(target)
        hiltPlugin.apply(target)
        testPlugin.apply(target)
    }

}