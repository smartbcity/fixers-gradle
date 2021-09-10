package city.smartb.fixers.gradle.kotlin

import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.extra

enum class Target {
    ALL,
    JVM,
    JS,
    META;

    companion object {
        private const val PROPERTY = "target"

        @JvmStatic
        fun shouldDefineTarget(project: ExtensionAware, targetToDefine: Target): Boolean {
            val currentTarget = currentTarget(project)
            val targetGroupCheck = when (currentTarget) {
                ALL -> true
                else -> false
            }
            return currentTarget == targetToDefine || currentTarget == META || targetGroupCheck
        }

		@JvmStatic
		fun currentTarget(project: ExtensionAware): Target {
			val value = project.find(PROPERTY).toString()
			return values().find { it.name.equals(value, ignoreCase = true) } ?: ALL
		}

        @JvmStatic
        fun shouldPublishTarget(project: ExtensionAware, targetToPublish: Target): Boolean {
            val currentTarget = currentTarget(project)
            if (currentTarget == META) {
                return targetToPublish == META
            }
            return shouldDefineTarget(project, targetToPublish)
        }

        private fun ExtensionAware.find(key: String) =
            if (extra.has(key)) extra.get(key) else null
    }
}
