pluginManagement {
  repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
    google()
  }
}
plugins {
//  id("de.fayard.refreshVersions") version "0.40.1"
}

rootProject.name = "sandbox"

includeBuild("../")
include(
  ":sandbox-object:object-domain",
  ":sandbox-object:object-api",
)
