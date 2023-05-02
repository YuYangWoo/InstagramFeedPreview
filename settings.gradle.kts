pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
rootProject.name = "InstagramFeedPreview"
include (":app")
include(":core:datastore")
include(":data")
include(":core:network")
include(":data:repository_impl")
include(":domain")
include(":domain:usecase")
include(":domain:repository")
