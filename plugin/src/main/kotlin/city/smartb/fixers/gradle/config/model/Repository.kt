package city.smartb.fixers.gradle.config.model

import org.gradle.api.Project
import org.gradle.internal.impldep.org.eclipse.jgit.lib.RepositoryBuilder
import java.lang.System.getenv
import java.net.URI

data class Repository(
	val id: String?,
	val name: String,
	val username: String,
	val password: String,
) {
	companion object

	val url: URI
		get() = URI.create(
//			if (id != null) {
//				"https://oss.sonatype.org/service/local/staging/deployByRepositoryId/$id/"
//			} else {
//				"https://oss.sonatype.org/service/local/staging/deploy/maven2/"
				"https://oss.sonatype.org/content/repositories/snapshots"
//			}
		)

}


fun Repository.Companion.sonatype(project: Project) = Repository(
	name = "sonatype",
	id = getenv("sonatype.id") ?: project.findProperty("sonatype.id")?.toString(),
	username = getenv("sonatypeUsername") ?:  project.findProperty("sonatype.username").toString(),
	password = getenv("sonatypePassword") ?: project.findProperty("sonatype.password").toString(),
)
