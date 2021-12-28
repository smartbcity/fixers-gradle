package city.smartb.gradle.config.model

import org.gradle.api.Project
import java.lang.System.getenv
import java.net.URI

data class Repository(
	val name: String,
	val username: String,
	val password: String,
	val version: String,
) {
	companion object

	val url: URI
		get() = URI.create(
			if (version.endsWith("-SNAPSHOT")) {
				"https://oss.sonatype.org/content/repositories/snapshots"
			} else {
				"https://oss.sonatype.org/service/local/staging/deploy/maven2"
			}
		)

}

fun Repository.Companion.sonatype(project: Project) = Repository(
	name = "sonatype",
	username = getenv("sonatypeUsername") ?:  project.findProperty("sonatype.username").toString(),
	password = getenv("sonatypePassword") ?: project.findProperty("sonatype.password").toString(),
	version = project.version.toString()
)
