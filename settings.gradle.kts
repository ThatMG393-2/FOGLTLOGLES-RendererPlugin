pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
		
		maven(url = "https://jitpack.io")
		maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
}

rootProject.name = "FOGLTLOGLES-RendererPlugin"

include(":app")