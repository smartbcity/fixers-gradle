rootProject.name = "fixers-gradle"

pluginManagement {
	repositories {
		gradlePluginPortal()
	}
}

include("config")
include("dependencies")
include("plugin")
