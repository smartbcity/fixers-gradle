package city.smartb.gradle.dependencies

import org.gradle.api.artifacts.Dependency

object FixersPluginVersions {
	const val fixers = "experimental-SNAPSHOT"
	const val kotlin = "1.6.1"
	const val springBoot = "2.6.0"
	const val npmPublish = "1.1.4"

}

object FixersVersions {

	object Logging {
		const val slf4j = "1.7.32"
	}

	object Spring {
		const val boot = FixersPluginVersions.springBoot
		const val data = FixersPluginVersions.springBoot
		const val function = "3.1.5"
	}

	object Test {
		const val cucumber = "7.0.0"
		const val junit = "5.8.1"
		const val junitPlateform = "1.8.1"
		const val assertj = "3.15.0"
	}

	object Kotlin {
		const val coroutines = "1.5.2"
		const val serialization = "1.3.1"
		const val ktor = "1.6.7"
	}

	const val d2 = "experimental-SNAPSHOT"
}

object FixersDependencies {
	object Jvm {
		object Kotlin {
			fun coroutines(scope: Scope) = scope.add(
				"org.jetbrains.kotlinx:kotlinx-coroutines-core:${FixersVersions.Kotlin.coroutines}",
				"org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${FixersVersions.Kotlin.coroutines}",
				"org.jetbrains.kotlinx:kotlinx-coroutines-reactive:${FixersVersions.Kotlin.coroutines}",
			)

			fun ktorClient(scope: Scope) = scope.add(
				"io.ktor:ktor-client-core:${FixersVersions.Kotlin.ktor}",
				"io.ktor:ktor-client-cio:${FixersVersions.Kotlin.ktor}",
				"io.ktor:ktor-client-auth:${FixersVersions.Kotlin.ktor}",
				"io.ktor:ktor-client-jackson:${FixersVersions.Kotlin.ktor}"
			)

			fun slf4j(scope: Scope) = scope.add(
				"org.slf4j:slf4j-api:${FixersVersions.Logging.slf4j}"
			)
		}

		object Test {
			fun cucumber(scope: Scope) = scope.add(
				"io.cucumber:cucumber-java8:${FixersVersions.Test.cucumber}",
				"io.cucumber:cucumber-junit-platform-engine:${FixersVersions.Test.cucumber}",
			)

			fun junit(scope: Scope) = scope.add(
				"org.junit.jupiter:junit-jupiter:${FixersVersions.Test.junit}",
				"org.junit.jupiter:junit-jupiter-api:${FixersVersions.Test.junit}",
				"org.junit.platform:junit-platform-suite:${FixersVersions.Test.junitPlateform}",
				"org.assertj:assertj-core:${FixersVersions.Test.assertj}"
			)
		}
	}


	object Common {
		fun test(scope: Scope) = scope.add(
			"org.jetbrains.kotlin:kotlin-test-common:${FixersPluginVersions.kotlin}",
			"org.jetbrains.kotlin:kotlin-test-annotations-common:${FixersPluginVersions.kotlin}"
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
