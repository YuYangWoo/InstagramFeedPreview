pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    includeBuild("build-logic")
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
include(":domain")
include(":core:network")
include(":domain:repository")
include(":domain:usecase")
include(":data:repository")
include(":feature")
include(":feature:main")
include(":feature:login")
include(":feature:board")
include(":domain:model")
include(":data:datasource")
include(":core:dto")
