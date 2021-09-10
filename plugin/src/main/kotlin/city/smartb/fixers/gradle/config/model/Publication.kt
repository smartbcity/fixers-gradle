package city.smartb.fixers.gradle.config.model

import org.gradle.api.Action
import org.gradle.api.publish.maven.MavenPom


class Publication(
	val configure: Action<MavenPom>
)