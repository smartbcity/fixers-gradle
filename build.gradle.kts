plugins {
	kotlin("jvm") version PluginVersions.kotlin apply false
	id("com.gradle.plugin-publish") version PluginVersions.gradlePublish apply false
}


allprojects {
	version = System.getenv("VERSION") ?: "experimental-SNAPSHOT"
	group = "city.smartb.fixers.gradle"
	repositories {
		mavenCentral()
		gradlePluginPortal()
	}
}
