package city.smartb.fixers.gradle.kotlin

import city.smartb.gradle.dependencies.FixersDependencies
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("UnstableApiUsage")
class JvmPlugin : Plugin<Project> {

	override fun apply(target: Project) {
		configureJvmCompilation(target)
	}

	private fun configureJvmCompilation(target: Project) {
		target.apply(plugin = "java")
		target.apply(plugin = "org.jetbrains.kotlin.jvm")

		target.tasks.withType<KotlinCompile>().configureEach {
			println("Configuring $name in project ${project.name}...")
			kotlinOptions {
				freeCompilerArgs = listOf("-Xjsr305=strict",  "-Xopt-in=kotlin.js.ExperimentalJsExport")
				jvmTarget = "11"
			}
		}

		target.dependencies {
			add("implementation", kotlin("reflect"))
			FixersDependencies.Jvm.Kotlin.coroutines{
				add("implementation", it)
			}
			FixersDependencies.Jvm.Test.junit{
				add("testImplementation", it)
			}
		}

		target.extensions.configure(JavaPluginExtension::class.java) {
			sourceCompatibility = JavaVersion.VERSION_11
			targetCompatibility = JavaVersion.VERSION_11
		}

		target.tasks.withType<Test> {
			useJUnitPlatform()
		}

	}
}
