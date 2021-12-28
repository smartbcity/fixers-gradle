

rootProject.name = "fixers-gradle"

pluginManagement {
	repositories {
		gradlePluginPortal()
		maven {
			url = uri("https://plugins.gradle.org/m2/")
		}
	}
}

include("config")
include("dependencies")
include("plugin")
