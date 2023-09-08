package city.smartb.fixers.gradle.sonar

import city.smartb.gradle.config.ConfigExtension
import city.smartb.gradle.config.fixers
import io.gitlab.arturbosch.detekt.Detekt
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
		target.afterEvaluate {
			val config = target.rootProject.extensions.fixers
			configureSonarQube(target)
			target.subprojects {
				configureJacoco()
				if(config?.detekt?.disable != true) {
					configureDetekt()
				}
			}
		}

	}

	private fun Project.configureDetekt() {
		plugins.apply("io.gitlab.arturbosch.detekt")
		extensions.configure(DetektExtension::class.java) {
			source.from(
				files(
					file("src")
						.listFiles()
						?.filter { it.isDirectory && it.name.endsWith("main", ignoreCase = true) }
				)
			)
			config.from(
				rootProject.files("detekt.yml")
			)
		}
		tasks.withType(Detekt::class.java).configureEach {
			reports {
				// Enable/Disable XML report (default: true)
				xml.required.set(true)
				xml.outputLocation.set(file("build/reports/detekt.xml"))
				// Enable/Disable HTML report (default: true)
				html.required.set(true)
				html.outputLocation.set(file("build/reports/detekt.html"))
				// Enable/Disable TXT report (default: true)
				txt.required.set(false)
				txt.outputLocation.set(file("build/reports/detekt.txt"))
				// Enable/Disable SARIF report (default: false)
				sarif.required.set(false)
				sarif.outputLocation.set(file("build/reports/detekt.sarif"))
				// Enable/Disable MD report (default: false)
				md.required.set(true)
				md.outputLocation.set(file("build/reports/detekt.md"))
			}
		}
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
					property ("detekt.sonar.kotlin.config.path", "${rootDir}/detekt.yml")

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
