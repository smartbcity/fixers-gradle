package city.smartb.fixers.gradle.d2

import city.smartb.fixers.gradle.config.fixers
import city.smartb.fixers.gradle.dependencies.Versions
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.register

class D2Plugin : Plugin<Project> {

	companion object {
		const val DOKKA_STORYBOOK = "dokkaStorybook"
		const val DOKKA_STORYBOOK_PARTIAL = "${DOKKA_STORYBOOK}Partial"
	}

	override fun apply(target: Project) {
		target.plugins.apply("org.jetbrains.dokka")
		target.subprojects {
			tasks {
				register<org.jetbrains.dokka.gradle.DokkaTask>(DOKKA_STORYBOOK_PARTIAL) {
					dependencies {
						plugins("city.smartb.d2:dokka-storybook-plugin:${Versions.d2}")
					}
				}
			}
		}
		target.afterEvaluate {
			target.extensions.fixers?.let { config ->
				target.tasks {
					register<org.jetbrains.dokka.gradle.DokkaCollectorTask>(DOKKA_STORYBOOK) {
						addChildTask(DOKKA_STORYBOOK_PARTIAL)
						addSubprojectChildTasks(DOKKA_STORYBOOK_PARTIAL)
						outputDirectory.set(config.d2.outputDirectory)
					}
				}
			}
		}

	}
}
