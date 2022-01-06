package city.smartb.fixers.gradle.publish

import city.smartb.fixers.gradle.kotlin.JvmPlugin
import city.smartb.gradle.config.model.Publication
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get

object PublishJvmPlugin {

	fun setupJVMPublish(project: Project, publication: Publication?) {
		project.plugins.withType(JvmPlugin::class.java) {
			setupJvmPublishJar(project)
			project.setupPublication(publication)
			project.setupExistingPublication(publication)
		}
	}

	private fun Project.setupExistingPublication(publication: Publication?) {
		val variantName = name
		configure<PublishingExtension> {
			this.publications.maybeCreate("")
			publications.all {
				val mavenPublication = this as? MavenPublication
				mavenPublication?.artifactId = getArtifactId(variantName, name)
				publication?.let { mavenPublication?.pom(publication.configure) }
				mavenPublication?.artifact(tasks["javadocJar"])
				mavenPublication?.artifact(tasks["sourcesJar"])
			}
		}
	}


	internal fun getArtifactId(projectName: String, publicationName: String): String {
		if(publicationName.endsWith("PluginMarkerMaven")) {
			return publicationName.replace("PluginMarkerMaven", ".gradle.plugin")
		}
		return projectName
	}

	private fun Project.setupPublication(publication: Publication?) {
		val publishing = project.extensions.getByType(PublishingExtension::class.java)
		extensions.findByType(JavaPluginExtension::class.java)?.let {
			publishing.publications {
				create<MavenPublication>("") {
					from(components["kotlin"])
					publication?.let { pom(publication.configure) }
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
