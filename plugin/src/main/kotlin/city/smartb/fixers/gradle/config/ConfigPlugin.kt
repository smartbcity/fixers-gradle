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
		val root: Project = target.rootProject
		target.afterEvaluate {
			target.logger.info("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^")
			target.logger.info("Target: ${target.name}")
			target.logger.info("Root: ${root.name}")
			target.logger.info("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^")
			root.extensions.fixersIfExists {
				root.extensions.configure(ConfigExtension::class.java) {
//						this.bundle = mainConfig.bundle
//						this.sonar = mainConfig.sonar
//						this.publication = mainConfig.publication
//						this.repository = mainConfig.repository
//						this.kt2Ts = mainConfig.kt2Ts
//						this.jdk = mainConfig.jdk
				}
			}
			root.extensions.fixers?.let { config ->
				target.logger.info("Fixers Configuration")
				target.logger.info("bundle.name: ${config.bundle.name}")
				target.logger.info("bundle.description: ${config.bundle.description}")
				target.logger.info("bundle.version: ${config.bundle.version}")
				target.logger.info("repository.name: ${config.repository.name}")
				target.logger.info("kt2Ts?.outputDirectory: ${config.kt2Ts.outputDirectory}")
				target.logger.info("kt2Ts?.inputDirectory: ${config.kt2Ts.inputDirectory}")
				target.logger.info("######################################################")

				target.configureKt2Ts(config)
			}
		}
	}

	private fun Project.configureKt2Ts(mainConfig: ConfigExtension?) {
		val target = this
		mainConfig?.kt2Ts?.let { config ->
			target.tasks {
				register<Delete>("cleanTsGen") {
					delete(config.outputDirectory)
				}

				register<Copy>("tsGen") {
					target.logger.info("*** Start tsGen")
					dependsOn("cleanTsGen")
					val inputDir = if(config.inputDirectory != null) {
						config.inputDirectory!!
					} else {
						"${this.project.buildDir.absolutePath}/js/packages/".also {
							target.logger.info("fixers.kt2Ts.inputDirectory is not set. Default value [$it] will be used.")
						}
					}
					from(inputDir) {
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

	private fun Project.createExtension() = extensions.findByType(ConfigExtension::class.java) ?: extensions.create(
		ConfigExtension.NAME,
		ConfigExtension::class.java,
		this@createExtension
	)
}
