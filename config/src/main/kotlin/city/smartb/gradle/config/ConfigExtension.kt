package city.smartb.gradle.config

import city.smartb.gradle.config.model.Bundle
import city.smartb.gradle.config.model.Jdk
import city.smartb.gradle.config.model.Kt2Ts
import city.smartb.gradle.config.model.Publication
import city.smartb.gradle.config.model.Repository
import city.smartb.gradle.config.model.Sonar
import city.smartb.gradle.config.model.smartB
import city.smartb.gradle.config.model.sonatype
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.publish.maven.MavenPom
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

/**
 * Retrieves the [fixers][city.smartb.fixers.gradle.fixers] extension.
 */
val ExtensionContainer.fixers: ConfigExtension?
	get() = try {
		getByName(ConfigExtension.NAME) as ConfigExtension?
	} catch (e: org.gradle.api.UnknownDomainObjectException) {
		null
	}

/**
 * Configures the [fixers][city.smartb.fixers.gradle.fixers] extension.
 */
fun Project.fixers(configure: Action<ConfigExtension>): Unit =
	(this as org.gradle.api.plugins.ExtensionAware).extensions.configure(ConfigExtension.NAME, configure)

/**
 * Configures the [fixers][city.smartb.fixers.gradle.fixers] extension if exists.
 */
fun ExtensionContainer.fixersIfExists(configure: Action<ConfigExtension>) {
	if (fixers != null) {
		configure(ConfigExtension.NAME, configure)
	}
}

fun PluginDependenciesSpec.fixers(module: String): PluginDependencySpec = id("city.smartb.fixers.gradle.${module}")


open class ConfigExtension(
	val project: Project
) {
	companion object {
		const val NAME: String = "fixers"
	}

	var bundle: Bundle = Bundle(
		name = project.name
	)

	var kt2Ts: Kt2Ts? = null

	var jdk: Jdk = Jdk(
		version = 11
	)

	var repository: Repository = Repository.sonatype(project)

	var publication: Publication? = null

	var sonar: Sonar = Sonar.smartB(project)
	var properties: MutableMap<String, Any> = mutableMapOf()

	fun bundle(configure: Action<Bundle>) {
		configure.execute(bundle)
		publication(pom(bundle))
	}

	fun kt2Ts(configure: Action<Kt2Ts>) {
		configure.execute(kt2Ts ?: Kt2Ts("platform/web/kotlin"))
	}

	fun sonar(configure: Action<Sonar>) {
		configure.execute(sonar)
	}

	fun jdk(configure: Action<Jdk>) {
		configure.execute(jdk)
	}

	fun publication(configure: Action<MavenPom>) {
		publication = Publication(configure)
	}

	fun repository(configure: Action<Repository>) {
		configure.execute(repository)
	}
}

fun pom(bundle: Bundle): Action<MavenPom> = Action {
	name.set(bundle.name)
	description.set(bundle.description)
	url.set(bundle.url)

	this.scm {
		url.set(bundle.url)
	}
	licenses {
		license {
			name.set("The Apache Software License, Version 2.0")
			url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
		}
	}
	developers {
		developer {
			id.set("SmartB")
			name.set("SmartB Team")
			organization.set("SmartB")
			organizationUrl.set("https://www.smartb.city")
		}
	}
}
