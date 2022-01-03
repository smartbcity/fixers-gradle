package city.smartb.fixers.gradle.publish

import city.smartb.fixers.gradle.config.ConfigPlugin
import city.smartb.gradle.config.ConfigExtension
import city.smartb.gradle.config.fixers
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.plugins.signing.SigningExtension
import org.gradle.plugins.signing.SigningPlugin
import java.lang.System.getenv

class PublishPlugin : Plugin<Project> {

	override fun apply(target: Project) {
		target.plugins.apply(MavenPublishPlugin::class.java)
		target.plugins.apply(ConfigPlugin::class.java)
		target.logger.info("Apply PublishPlugin to ${target.name}")
		target.afterEvaluate {
			val fixers = target.extensions.fixers
			fixers?.let { fixersConfig ->
				setupPublishing(fixersConfig)
				setupSign()
			}
		}
	}

	private fun Project.setupPublishing(fixersConfig: ConfigExtension) {
		val publishing = project.extensions.getByType(PublishingExtension::class.java)
		val publication = fixersConfig.publication
		val repository = fixersConfig.repository
		publishing.repositories {
			maven {
				name = repository.name
				url = repository.url
				credentials {
					username = repository.username
					password = repository.password
				}
			}
		}
		PublishMppPlugin.setupMppPublish(this, publication)
		PublishJvmPlugin.setupJVMPublish(this, publication)
	}

	private fun Project.setupSign() {
		val inMemoryKey = getenv("signingKey") ?: findProperty("signingKey")?.toString()
		val password = getenv("signingPassword") ?: findProperty("signingPassword")?.toString()
		if (inMemoryKey == null) {
			logger.warn("No signing config provided, skip signing")
			return
		}
		plugins.apply(SigningPlugin::class.java)
		extensions.getByType(SigningExtension::class.java).apply {
			isRequired = true
			useInMemoryPgpKeys(inMemoryKey, password)
			sign(
				extensions.getByType(PublishingExtension::class.java).publications
			)
		}
	}
}
