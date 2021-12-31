package city.smartb.fixers.gradle.publish

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class PublishPluginTest {


	@Test
	fun takeUnless() {
		val projectName = "f2-client"
		val values = listOf(
			"js" to "f2-client-js",
			"kotlinMultiplatform" to "f2-client"
		)

		values.forEach { (publication, expected) ->
			val result = PublishPlugin().getArtifactId(projectName, publication)
			Assertions.assertThat(result).isEqualTo(expected)

		}
	}
}
