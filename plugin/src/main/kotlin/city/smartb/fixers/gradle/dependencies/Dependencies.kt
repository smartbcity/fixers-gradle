package city.smartb.fixers.gradle.dependencies

object PluginVersions {
	const val kotlin = "1.6.0"
}

object Versions {

	const val junit = "5.7.0"
	const val assertj = "3.15.0"

	const val coroutines = "1.5.2"
	const val kserialization = "1.1.0"

	const val d2 = "0.1.1-SNAPSHOT"
}

object Dependencies {
	object jvm {
		val coroutines = arrayOf(
			"org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}",
			"org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${Versions.coroutines}",
			"org.jetbrains.kotlinx:kotlinx-coroutines-reactive:${Versions.coroutines}",
			"org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:${Versions.coroutines}"
		)
		val test = arrayOf(
			"org.junit.jupiter:junit-jupiter:${Versions.junit}",
			"org.junit.jupiter:junit-jupiter-api:${Versions.junit}",
			"org.assertj:assertj-core:${Versions.assertj}"
		)
	}

	object common {
		val coroutines = arrayOf(
			"org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
		)
		val test =  arrayOf(
			"org.jetbrains.kotlin:kotlin-test-common:${PluginVersions.kotlin}",
			"org.jetbrains.kotlin:kotlin-test-annotations-common:${PluginVersions.kotlin}"
		)
		val kserialization = arrayOf(
			"org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.kserialization}",
			"org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kserialization}"
		)
	}
}
