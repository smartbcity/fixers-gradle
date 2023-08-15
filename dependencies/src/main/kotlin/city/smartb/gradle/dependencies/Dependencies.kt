package city.smartb.gradle.dependencies

import org.gradle.api.artifacts.Dependency

object FixersPluginVersions {
	const val kotlin = "1.9.0"
	const val springBoot = "3.1.2"
	const val npmPublish = "3.4.1"
	/**
	 * com.google.devtools.ksp
	 */
	const val ksp = "1.9.0-1.0.13"
	/**
	 * org.graalvm.buildtools.native.gradle.plugin
	 */
	const val graalvm = "0.9.24"

	val fixers = FixersPluginVersions::class.java.`package`.implementationVersion!!
}

object FixersVersions {
	object Logging {
		const val slf4j = "2.0.7"
	}

	object Spring {
		const val boot = FixersPluginVersions.springBoot
		const val data = "3.1.2"
		const val framework = "6.0.11"
	 	const val jakartaPersistence = "3.1.0"
	}

	object Json {
		const val jackson = "2.15.2"
		const val jacksonKotlin = "2.15.2"
	}

	object Test {
		const val cucumber = "7.13.0"
		const val junit = "5.10.0"
		const val junitPlatform = "1.10.0"
		const val assertj = "3.24.2"
		const val testcontainers = "1.18.3"
	}

	object Kotlin {
		const val coroutines = "1.7.3"
		const val serialization = "1.5.1"
		const val ktor = "2.3.3"
	}
}

object FixersDependencies {
	object Jvm {
		object Json {
			fun jackson(scope: Scope) = scope.add(
				"com.fasterxml.jackson.module:jackson-module-kotlin:${FixersVersions.Json.jacksonKotlin}"
			)
			fun kSerialization(scope: Scope) = Common.Kotlin.serialization(scope)
		}

		object Logging {
			fun slf4j(scope: Scope) = scope.add(
				"org.slf4j:slf4j-api:${FixersVersions.Logging.slf4j}"
			)
		}

		object Spring {
			fun dataCommons(scope: Scope) = scope.add(
				"jakarta.persistence:jakarta.persistence-api:${FixersVersions.Spring.jakartaPersistence}",
				"org.springframework:spring-context:${FixersVersions.Spring.framework}",
				"org.springframework.data:spring-data-commons:${FixersVersions.Spring.data}"
			)
			fun autoConfigure(scope: Scope, ksp: Scope) = scope.add(
				"org.springframework.boot:spring-boot-autoconfigure:${FixersVersions.Spring.boot}"
			).also {
				ksp.add(
					"org.springframework.boot:spring-boot-configuration-processor:${FixersVersions.Spring.boot}"
				)
			}
		}

		object Kotlin {
			fun coroutines(scope: Scope) = scope.add(
				"org.jetbrains.kotlinx:kotlinx-coroutines-core:${FixersVersions.Kotlin.coroutines}",
				"org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${FixersVersions.Kotlin.coroutines}",
				"org.jetbrains.kotlinx:kotlinx-coroutines-reactive:${FixersVersions.Kotlin.coroutines}",
			)
		}

		object Test {
			fun cucumber(scope: Scope) = scope.add(
				"io.cucumber:cucumber-java:${FixersVersions.Test.cucumber}",
				"io.cucumber:cucumber-java8:${FixersVersions.Test.cucumber}",
				"io.cucumber:cucumber-junit-platform-engine:${FixersVersions.Test.cucumber}",
			)

			fun junit(scope: Scope) = scope.add(
				"org.junit.jupiter:junit-jupiter:${FixersVersions.Test.junit}",
				"org.junit.jupiter:junit-jupiter-api:${FixersVersions.Test.junit}",
				"org.junit.platform:junit-platform-suite:${FixersVersions.Test.junitPlatform}",
				"org.assertj:assertj-core:${FixersVersions.Test.assertj}",
				"org.jetbrains.kotlinx:kotlinx-coroutines-test:${FixersVersions.Kotlin.coroutines}"
			)
		}
	}

	object Common {
		fun test(scope: Scope) = scope.add(
			"org.jetbrains.kotlin:kotlin-test-common:${FixersPluginVersions.kotlin}",
			"org.jetbrains.kotlin:kotlin-test-annotations-common:${FixersPluginVersions.kotlin}",
			"org.jetbrains.kotlinx:kotlinx-coroutines-test:${FixersVersions.Kotlin.coroutines}"
		)

		object Kotlin {
			fun coroutines(scope: Scope) = scope.add(
				"org.jetbrains.kotlinx:kotlinx-coroutines-core:${FixersVersions.Kotlin.coroutines}"
			)

			fun serialization(scope: Scope) = scope.add(
				"org.jetbrains.kotlinx:kotlinx-serialization-core:${FixersVersions.Kotlin.serialization}",
				"org.jetbrains.kotlinx:kotlinx-serialization-json:${FixersVersions.Kotlin.serialization}"
			)
		}
	}
}

typealias Scope = (dependencyNotation: Any) -> Dependency?

fun Scope.add(vararg deps: String): Scope {
	deps.forEach { this(it) }
	return this
}
