pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Barrion"
include(":app")
include(":domain")
include(":data")
include(":presentation")
include(":core:ui")
include(":core:common")
include(":feature:auth")
include(":feature:onboarding")
include(":feature:sales")
include(":feature:menu")
include(":feature:order")
include(":feature:staff")
