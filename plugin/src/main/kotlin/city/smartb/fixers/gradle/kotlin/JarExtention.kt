package city.smartb.fixers.gradle.kotlin

import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.attributes
import org.gradle.kotlin.dsl.withType


fun Project.setupJarInfo() {
	tasks.withType<Jar> {
		manifest {
			attributes(
				"Implementation-Title" to project.name,
				"Implementation-Version" to project.version
			)
		}
	}
}