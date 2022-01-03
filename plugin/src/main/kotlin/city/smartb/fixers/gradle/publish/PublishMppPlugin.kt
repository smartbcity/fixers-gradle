package city.smartb.fixers.gradle.publish

import city.smartb.fixers.gradle.kotlin.MppPlugin
import city.smartb.gradle.config.model.Publication
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get

object PublishMppPlugin {

	fun setupMppPublish(project: Project, publication: Publication?) {
		project.plugins.withType(MppPlugin::class.java) {
			project.setupMppPublishJar()
			project.setupPublication(publication)
		}
	}

	private fun Project.setupPublication(publication: Publication?) {
		val variantName = name
		configure<PublishingExtension> {
			publications.all {
				val mavenPublication = this as? MavenPublication
				mavenPublication?.artifactId = getArtifactId(variantName, name)
				publication?.let { mavenPublication?.pom(publication.configure) }
				mavenPublication?.artifact(tasks["javadocJar"])
			}
		}
	}

	internal fun getArtifactId(projectName: String, publicationName: String): String {
		return "${projectName}${"-$publicationName".takeUnless { "kotlinMultiplatform" in publicationName }.orEmpty()}"
	}

	private fun Project.setupMppPublishJar() {
		tasks.register("javadocJar", Jar::class.java) {
			archiveClassifier.set("javadoc")
		}
	}
}
