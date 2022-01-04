package city.smartb.fixers.gradle.kotlin

import city.smartb.fixers.gradle.config.ConfigPlugin
import city.smartb.gradle.config.fixers
import city.smartb.gradle.config.model.Jdk
import city.smartb.gradle.dependencies.FixersDependencies
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.tasks.Jar
import org.gradle.jvm.toolchain.JavaLanguageVersion
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
		target.plugins.apply(ConfigPlugin::class.java)
		val fixersConfig = target.extensions.fixers

		val jdkVersion = fixersConfig?.jdk?.version ?: Jdk.VERSION_DEFAULT
		target.tasks.withType<KotlinCompile>().configureEach {
			println("Configuring $name in project ${project.name}...")
			kotlinOptions {
				freeCompilerArgs = listOf("-Xjsr305=strict",  "-Xopt-in=kotlin.js.ExperimentalJsExport")
				jvmTarget = jdkVersion.toString()
			}
		}
		target.plugins.withType(JavaPlugin::class.java).whenPluginAdded {
			target.extensions.configure(JavaPluginExtension::class.java) {
				toolchain.languageVersion.set(JavaLanguageVersion.of(jdkVersion))
			}
		}

		target.dependencies {
			FixersDependencies.Jvm.Kotlin.reflect{
				add("implementation", it)
			}
			FixersDependencies.Jvm.Kotlin.coroutines{
				add("implementation", it)
			}
			FixersDependencies.Jvm.Test.junit{
				add("testImplementation", it)
			}
		}

		target.tasks.withType<Test> {
			useJUnitPlatform()
		}

	}

}
