package city.smartb.fixers.gradle.config

import city.smartb.gradle.config.ConfigExtension
import city.smartb.gradle.config.fixers
import city.smartb.gradle.config.fixersIfExists
import org.gradle.api.Plugin
import org.gradle.api.Project

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


	private fun Project.createExtension() = extensions.findByType(ConfigExtension::class.java) ?: extensions.create(
		ConfigExtension.NAME,
		ConfigExtension::class.java,
		this@createExtension
	)
}
