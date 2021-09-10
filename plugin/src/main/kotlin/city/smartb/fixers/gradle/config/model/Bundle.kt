package city.smartb.fixers.gradle.config.model

data class Bundle(
	var name: String,
	var id: String? = null,
	var description: String? = null,
	var version: String? = null,
	var url: String? = null
)
