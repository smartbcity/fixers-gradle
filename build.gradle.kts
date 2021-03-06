plugins {
	kotlin("jvm") version PluginVersions.kotlin apply false
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


subprojects {
	plugins.withType(JavaPlugin::class.java).whenPluginAdded {
		extensions.configure(JavaPluginExtension::class.java) {
			toolchain.languageVersion.set(JavaLanguageVersion.of(11))
		}
	}
	tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "11"
		}

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
