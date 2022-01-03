package city.smartb.fixers.gradle.publish

import city.smartb.fixers.gradle.kotlin.JvmPlugin
import city.smartb.gradle.config.model.Publication
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get

object PublishJvmPlugin {

	fun setupJVMPublish(project: Project, publication: Publication?) {
		project.plugins.withType(JvmPlugin::class.java) {
			setupJvmPublishJar(project)
			project.setupPublication(publication)
		}
	}

	private fun Project.setupPublication(publication: Publication?) {
		val publishing = project.extensions.getByType(PublishingExtension::class.java)
		extensions.findByType(JavaPluginExtension::class.java)?.let {
			publishing.publications {
				create<MavenPublication>("") {
					from(components["kotlin"])
					publication?.let { pom(publication.configure) }
					artifact(tasks["javadocJar"])
					artifact(tasks["sourcesJar"])
				}
			}
		}
	}

	private fun setupJvmPublishJar(project: Project) {
		project.plugins.withType(JvmPlugin::class.java) {
			project.tasks.register("javadocJar", Jar::class.java) {
				val javadoc = project.tasks.named("javadoc")
				dependsOn.add(javadoc)
				archiveClassifier.set("javadoc")
				from(javadoc)
			}

			project.tasks.register("sourcesJar", Jar::class.java) {
				archiveClassifier.set("sources")
				val sourceSets = project.properties["sourceSets"] as SourceSetContainer
				from(sourceSets["main"].allSource)
			}
		}
	}
}
