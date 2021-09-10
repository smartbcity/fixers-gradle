package city.smartb.fixers.gradle.config.model

import org.gradle.api.Project

data class Sonar(
	var login: String,
	var url: String
) {
	companion object
}

fun Sonar.Companion.smartB(project: Project) = Sonar(
	url = "https://sonarqube.smartb.city",
	login = System.getenv("sonar.username") ?: project.findProperty("sonar.username").toString(),
)