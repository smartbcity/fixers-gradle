

rootProject.name = "fixers-gradle"

pluginManagement {
	repositories {
		maven(url = "../local-plugin-repository")
		gradlePluginPortal()
	}
}

include("config")
include("dependencies")
include("plugin")
