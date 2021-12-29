plugins {
	`kotlin-dsl`
	kotlin("jvm")
	id("com.gradle.plugin-publish")
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${PluginVersions.kotlin}")
	implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:${PluginVersions.kotlin}")

	implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${PluginVersions.detekt}")
	implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:${PluginVersions.sonarQube}")

	api(project(":dependencies"))
	api(project(":config"))
}

pluginBundle {
	website = "https://github.com/smartbcity/fixers-gradle"
	vcsUrl = "https://github.com/smartbcity/fixers-gradle"
	tags = listOf("SmartB", "Fixers", "kotlin", "mpp", "jvm")
	mavenCoordinates {
		groupId = "city.smartb.fixers.gradle"
	}
}

gradlePlugin {
	plugins {
		create("city.smartb.fixers.gradle.config") {
			id = "city.smartb.fixers.gradle.config"
			implementationClass = "city.smartb.fixers.gradle.config.ConfigPlugin"
			displayName = "Fixers Gradle Config"
			description = "Ease the configuration of Kotlin Fixers Project."
		}
		create("city.smartb.fixers.gradle.dependencies") {
			id = "city.smartb.fixers.gradle.dependencies"
			implementationClass = "city.smartb.fixers.gradle.dependencies.DependenciesPlugin"
			displayName = "Fixers Dependencies version"
			description = "Register fixers dependencies version."
		}
		create("city.smartb.fixers.gradle.kotlin.jvm") {
			id = "city.smartb.fixers.gradle.kotlin.jvm"
			implementationClass = "city.smartb.fixers.gradle.kotlin.JvmPlugin"
			displayName = "Fixers Gradle Kotlin JVM"
			description = "Ease the configuration of Kotlin Jvm Module."
		}
		create("city.smartb.fixers.gradle.kotlin.mpp") {
			id = "city.smartb.fixers.gradle.kotlin.mpp"
			implementationClass = "city.smartb.fixers.gradle.kotlin.MppPlugin"
			displayName = "Fixers Gradle Kotlin MPP"
			description = "Ease the configuration of Kotlin Multiplateform Plugin."
		}
		create("city.smartb.fixers.gradle.publish") {
			id = "city.smartb.fixers.gradle.publish"
			implementationClass = "city.smartb.fixers.gradle.publish.PublishPlugin"
			displayName = "Fixers Gradle publish"
			description = "Ease the configuration of maven publication."
		}
		create("city.smartb.fixers.gradle.sonar") {
			id = "city.smartb.fixers.gradle.sonar"
			implementationClass = "city.smartb.fixers.gradle.sonar.SonarPlugin"
			displayName = "Fixers Gradle Sonar"
			description = "Ease the configuration of static code analysis with sonarqube and detekt."
		}
	}
}

apply(from = rootProject.file("gradle/publishing.gradle"))
