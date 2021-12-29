package city.smartb.fixers.gradle.config

import city.smartb.gradle.config.ConfigExtension
import city.smartb.gradle.config.fixers
import city.smartb.gradle.config.fixersIfExists
import org.gradle.api.Plugin
import org.gradle.api.Project

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

	}

	private fun Project.createExtension() = extensions.findByType(ConfigExtension::class.java) ?: extensions.create(
		ConfigExtension.NAME,
		ConfigExtension::class.java,
		this@createExtension
	)
}
