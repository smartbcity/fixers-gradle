plugins {
	kotlin("jvm") version PluginVersions.kotlinDsl apply false
	id("com.gradle.plugin-publish") version PluginVersions.gradlePublish apply false
}

tasks.withType<JavaCompile> {
	val toolchain = the<JavaPluginExtension>().toolchain
	val javaToolchainService = the<JavaToolchainService>()
	toolchain.languageVersion.set(JavaLanguageVersion.of(11))
	tasks.withType<JavaExec>().configureEach {
		javaLauncher.set(javaToolchainService.launcherFor(toolchain))
	}
}

allprojects {
	version = System.getenv("VERSION") ?: "experimental-SNAPSHOT"
	group = "city.smartb.fixers.gradle"
	repositories {
		mavenCentral()
		gradlePluginPortal()
	}
}


subprojects {
	tasks.withType<Jar> {
		manifest {
			attributes(
				"Implementation-Title" to project.name,
				"Implementation-Version" to project.version
			)
		}
	}
	tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
		kotlinOptions {
			languageVersion = "1.4"
		}
	}
}

