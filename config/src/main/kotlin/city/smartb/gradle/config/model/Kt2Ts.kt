package city.smartb.gradle.config.model

data class Kt2Ts(
    var outputDirectory: String,
    var inputDirectory: String? = null,
    var additionalCleaning: Map<String, List<Pair<Regex, String>>> = emptyMap()
)
