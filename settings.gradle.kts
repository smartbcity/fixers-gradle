

rootProject.name = "fixers-gradle"

pluginManagement {
	repositories {
		maven(url = "../local-plugin-repository")
		gradlePluginPortal()
	}
}

includeBuild("dependencies")
include("plugin")
