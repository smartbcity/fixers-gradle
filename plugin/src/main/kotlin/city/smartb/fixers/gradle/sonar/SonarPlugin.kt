package city.smartb.fixers.gradle.sonar

import city.smartb.fixers.gradle.config.ConfigExtension
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.sonarqube.gradle.SonarQubeExtension

class SonarPlugin : Plugin<Project> {
	override fun apply(target: Project) {
		configureSonarQube(target)
		target.subprojects {
			configureJacoco()
			configureDetekt()
		}
	}

	private fun Project.configureDetekt() {
		plugins.apply("io.gitlab.arturbosch.detekt")
		extensions.configure(DetektExtension::class.java) {
			source = files(
				file("src")
					.listFiles()
					?.filter { it.isDirectory && it.name.endsWith("main", ignoreCase = true) }
			)
			config = rootProject.files("detekt.yml")
			reports {
				xml {
					enabled = true
				}
				html {
					enabled = true
				}
			}
		}
//		dependencies.add(CONFIGURATION_DETEKT_PLUGINS,
//			"io.gitlab.arturbosch.detekt:detekt-formatting:${PluginVersions.detekt}")
	}

	private fun configureSonarQube(target: Project) {
		target.plugins.apply("org.sonarqube")
		target.afterEvaluate {
			target.extensions.configure(SonarQubeExtension::class.java) {
				val fixers = target.extensions.findByType(ConfigExtension::class.java)
				properties {
					property("sonar.projectKey", fixers?.bundle?.id as Any)
					property("sonar.projectName", fixers.bundle.name)
					property("sonar.host.url", fixers.sonar.url)
					property("sonar.login", fixers.sonar.login)

					property("sonar.verbose", true)
					property("sonar.coverage.jacoco.xmlReportPaths",
						"${target.rootDir}/**/build/reports/jacoco/test/jacocoTestReport.xml")
				}
			}
		}
	}

	private fun Project.configureJacoco() {
		plugins.withType(JavaPlugin::class.java).whenPluginAdded {
			plugins.apply("jacoco")
			extensions.configure(JacocoPluginExtension::class.java) {
				toolVersion = "0.8.7"
			}
			tasks.withType<JacocoReport> {
				isEnabled = true
				reports {
					html.required.set(true)
					xml.required.set(true)
				}
				dependsOn(tasks.named("test"))
			}
			tasks.withType<Test> {
				useJUnitPlatform()
				finalizedBy(tasks.named("jacocoTestReport"))
			}
		}
	}
}
