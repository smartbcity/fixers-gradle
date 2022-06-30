package city.smartb.fixers.gradle.kotlin

import city.smartb.gradle.config.fixers
import city.smartb.gradle.config.model.Jdk
import city.smartb.gradle.dependencies.FixersDependencies
import city.smartb.gradle.dependencies.FixersPluginVersions
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class MppPlugin : Plugin<Project> {

	override fun apply(target: Project) {
		setupMultiplatformLibrary(target)
		setupJvmTarget(target)
		setupJsTarget(target)
		target.setupJarInfo()
	}

	private fun setupMultiplatformLibrary(target: Project) {
		target.apply(plugin = "org.jetbrains.kotlin.multiplatform")

		target.tasks.withType<KotlinCompile>().configureEach {
			kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.js.ExperimentalJsExport"
		}

		target.extensions.configure(KotlinMultiplatformExtension::class.java) {
			sourceSets {
				maybeCreate("commonMain").dependencies {
					implementation(kotlin("reflect"))
					FixersDependencies.Common.Kotlin.coroutines(::api)
					FixersDependencies.Common.Kotlin.serialization(::api)
				}
				maybeCreate("commonTest").dependencies {
					FixersDependencies.Common.test(::implementation)
				}
			}
		}
	}

	private fun setupJvmTarget(project: Project) {
		val fixersConfig = project.extensions.fixers
		project.kotlin {
			jvm {
				compilations.all {
					val jdkVersion = fixersConfig?.jdk?.version ?: Jdk.VERSION_DEFAULT
					kotlinOptions.jvmTarget = jdkVersion.toString()
					kotlinOptions.languageVersion = FixersPluginVersions.kotlin.substringBeforeLast(".")
				}
			}
			sourceSets.getByName("jvmMain") {
				dependencies {
					implementation(kotlin("reflect"))
					FixersDependencies.Jvm.Kotlin.coroutines(::implementation)
				}
			}
			sourceSets.getByName("jvmTest") {
				dependencies {
					FixersDependencies.Jvm.Test.junit (::implementation)
				}
			}
		}
	}

	private fun setupJsTarget(project: Project) {
		project.apply<MppJsPlugin>()
	}

	private fun Project.kotlin(action: Action<KotlinMultiplatformExtension>) {
		extensions.configure(KotlinMultiplatformExtension::class.java, action)
	}
}
