pluginManagement {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }

  resolutionStrategy {
    eachPlugin {
      when (requested.id.id) {
        "com.android.application" -> {
          useModule("com.android.tools.build:gradle:9.1.1")
        }
      }
    }
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "My Application"

include(":app")


