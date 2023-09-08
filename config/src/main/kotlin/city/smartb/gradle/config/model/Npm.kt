package city.smartb.gradle.config.model

data class Npm(
	var publish: Boolean = true,
	var organization: String = "smartb",
	var clean: Boolean = true,
	var version: String? = null,
)