package ext

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

internal fun Project.getVersionCatalog(name: String = "libs") = extensions.getByType(VersionCatalogsExtension::class).named(name)