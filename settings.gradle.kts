/*
 * Copyright (c) 2023-2023. Encept Ltd Company, https://encept.co
 * contact: support@encept.co
 */

pluginManagement {
    repositories {
        google()
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

rootProject.name = "muqataa"
include(":app")
