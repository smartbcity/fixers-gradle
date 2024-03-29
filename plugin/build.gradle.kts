plugins {
	`kotlin-dsl`
	kotlin("jvm")
	id("com.gradle.plugin-publish")
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${PluginVersions.kotlin}")
	implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:${PluginVersions.kotlin}")

	implementation("dev.petuska.npm.publish:dev.petuska.npm.publish.gradle.plugin:${PluginVersions.npmPublish}")

	implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${PluginVersions.detekt}")
	implementation("org.jetbrains.dokka:dokka-gradle-plugin:${PluginVersions.dokka}")
	implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:${PluginVersions.sonarQube}")

	api(project(":dependencies"))
	api(project(":config"))

	testImplementation("org.junit.jupiter:junit-jupiter:${Versions.junit}")
	testImplementation("org.junit.jupiter:junit-jupiter-api:${Versions.junit}")
	testImplementation("org.assertj:assertj-core:${Versions.assert4j}")
}

gradlePlugin {
	website = "https://github.com/smartbcity/fixers-gradle"
	vcsUrl = "https://github.com/smartbcity/fixers-gradle"
	plugins {
		create("city.smartb.fixers.gradle.config") {
			id = "city.smartb.fixers.gradle.config"
			implementationClass = "city.smartb.fixers.gradle.config.ConfigPlugin"
			displayName = "Fixers Gradle Config"
			description = "Ease the configuration of Kotlin Fixers Project."
			tags = listOf("SmartB", "Fixers", "kotlin", "mpp", "jvm", "js", "wasm")
		}
		create("city.smartb.fixers.gradle.dependencies") {
			id = "city.smartb.fixers.gradle.dependencies"
			implementationClass = "city.smartb.fixers.gradle.dependencies.DependenciesPlugin"
			displayName = "Fixers Dependencies version"
			description = "Register fixers dependencies version."
			tags = listOf("SmartB", "Fixers", "kotlin", "mpp", "jvm", "js", "wasm")
		}
		create("city.smartb.fixers.gradle.kotlin.jvm") {
			id = "city.smartb.fixers.gradle.kotlin.jvm"
			implementationClass = "city.smartb.fixers.gradle.kotlin.JvmPlugin"
			displayName = "Fixers Gradle Kotlin JVM"
			description = "Ease the configuration of Kotlin Jvm Module."
			tags = listOf("SmartB", "Fixers", "kotlin", "mpp", "jvm", "js", "wasm")
		}
		create("city.smartb.fixers.gradle.kotlin.mpp") {
			id = "city.smartb.fixers.gradle.kotlin.mpp"
			implementationClass = "city.smartb.fixers.gradle.kotlin.MppPlugin"
			displayName = "Fixers Gradle Kotlin MPP"
			description = "Ease the configuration of Kotlin Multiplateform Plugin."
			tags = listOf("SmartB", "Fixers", "kotlin", "mpp", "jvm", "js", "wasm")
		}
		create("city.smartb.fixers.gradle.publish") {
			id = "city.smartb.fixers.gradle.publish"
			implementationClass = "city.smartb.fixers.gradle.publish.PublishPlugin"
			displayName = "Fixers Gradle publish"
			description = "Ease the configuration of maven publication."
			tags = listOf("SmartB", "Fixers", "kotlin", "mpp", "jvm", "js", "wasm")
		}
		create("city.smartb.fixers.gradle.npm") {
			id = "city.smartb.fixers.gradle.npm"
			implementationClass = "city.smartb.fixers.gradle.npm.NpmPlugin"
			displayName = "Fixers Gradle publish npm"
			description = "Ease the configuration of npm publication."
			tags = listOf("SmartB", "Fixers", "kotlin", "mpp", "jvm", "js", "wasm")
		}
		create("city.smartb.fixers.gradle.sonar") {
			id = "city.smartb.fixers.gradle.sonar"
			implementationClass = "city.smartb.fixers.gradle.sonar.SonarPlugin"
			displayName = "Fixers Gradle Sonar"
			description = "Ease the configuration of static code analysis with sonarqube and detekt."
			tags = listOf("SmartB", "Fixers", "kotlin", "mpp", "jvm", "js", "wasm")
		}
	}
}

apply(from = rootProject.file("gradle/publishing_plugin.gradle"))

tasks.withType<Test> {
	useJUnitPlatform()
}
