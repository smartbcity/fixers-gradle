package city.smartb.fixers.gradle.config

import city.smartb.gradle.config.ConfigExtension
import city.smartb.gradle.config.fixers
import city.smartb.gradle.config.fixersIfExists
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.register

class ConfigPlugin : Plugin<Project> {
	override fun apply(target: Project) {
		val mainConfig = target.createExtension()
		target.allprojects {
			target.afterEvaluate {
				extensions.fixersIfExists {
					extensions.configure(ConfigExtension::class.java) {
						this.bundle = mainConfig.bundle
						this.sonar = mainConfig.sonar
						this.publication = mainConfig.publication
						this.repository = mainConfig.repository
					}
				}
				extensions.fixers?.let { config ->
					logger.debug("Fixers Configuration")
					logger.debug(config.bundle.name)
					logger.debug(config.bundle.description)
					logger.debug(config.bundle.version)
					logger.debug(config.repository.name)
					logger.debug("***************************************************")
				}
			}
		}

		mainConfig.kt2Ts?.let { config ->
			target.afterEvaluate {
				target.tasks {
					register<Delete>("cleanTsGen") {
						delete(config.outputDirectory)
					}

					register<Copy>("tsGen") {
						dependsOn("cleanTsGen")
						from("${this.project.buildDir.absolutePath}/js/packages/") {
							exclude("*-test")
						}
						into(config.outputDirectory)
						includeEmptyDirs = false

						val cleaning = listOf(
							".d.ts" to listOf(
								Regex("""(?m).*__doNotImplementIt.*\n""") to "",
								Regex("""kotlin.js.""") to "",
								Regex("""org.w3c.dom.url.""") to "",
								Regex("""org.w3c.dom.""") to "",
								Regex(""" any/\* ([^*/]*) \*/""") to " $1",
								Regex("""type Nullable<T> = T \| null \| undefined\n""") to ""
							),
							"package.json" to listOf(
								Regex("""("devDependencies": \{)(.|\n)*?(},)""") to "$1$3"
							)
						)

						eachFile {
							cleaning.forEach { (suffix, changes) ->
								if (file.name.endsWith(suffix)) {
									val content = file.readText()

									val newContent = changes.fold(content) { acc, (old, new) ->
										acc.replace(old, new)
									}

									if (newContent != content) {
										file.writeText(newContent)
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private fun Project.createExtension() = extensions.findByType(ConfigExtension::class.java) ?: extensions.create(
		ConfigExtension.NAME,
		ConfigExtension::class.java,
		this@createExtension
	)
}
