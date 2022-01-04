package city.smartb.fixers.gradle.kotlin

import city.smartb.gradle.dependencies.FixersDependencies
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
	}

	private fun setupMultiplatformLibrary(target: Project) {
		target.apply(plugin = "org.jetbrains.kotlin.multiplatform")

		target.tasks.withType<KotlinCompile>().configureEach {
			kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.js.ExperimentalJsExport"
		}

		target.extensions.configure(KotlinMultiplatformExtension::class.java) {
			sourceSets {
				maybeCreate("commonMain").dependencies {
					FixersDependencies.Common.Kotlin.reflect(::api)
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
		project.kotlin {
			jvm {
				compilations.all {
					kotlinOptions.jvmTarget = "11"
				}
			}
			sourceSets.getByName("jvmMain") {
				dependencies {
					FixersDependencies.Common.Kotlin.reflect(::api)
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
