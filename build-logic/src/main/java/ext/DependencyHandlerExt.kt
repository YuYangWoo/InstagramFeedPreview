package ext

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Provider

@JvmName("kaptMinimalExternalModuleDependency")
internal fun DependencyHandler.kapt(dependencyNotation: Provider<MinimalExternalModuleDependency>) = add("kapt", dependencyNotation)

@JvmName("implementationDependency")
internal fun DependencyHandler.implementation(dependencyNotation: Dependency) = add("implementation", dependencyNotation)
@JvmName("implementationMinimalExternalModuleDependency")
internal fun DependencyHandler.implementation(dependencyNotation: Provider<MinimalExternalModuleDependency>) = add("implementation", dependencyNotation)
@JvmName("implementationExternalModuleDependencyBundle")
internal fun DependencyHandler.implementation(dependencyNotation: Provider<ExternalModuleDependencyBundle>) = add("implementation", dependencyNotation)

@JvmName("debugImplementationMinimalExternalModuleDependency")
internal fun DependencyHandler.debugImplementation(dependencyNotation: Provider<MinimalExternalModuleDependency>) = add("debugImplementation", dependencyNotation)
@JvmName("debugImplementationExternalModuleDependencyBundle")
internal fun DependencyHandler.debugImplementation(dependencyNotation: Provider<ExternalModuleDependencyBundle>) = add("debugImplementation", dependencyNotation)