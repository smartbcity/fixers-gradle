package city.smartb.fixers.gradle.npm.task

import city.smartb.fixers.gradle.config.buildCleaningRegex
import city.smartb.fixers.gradle.config.cleanProject
import city.smartb.gradle.config.fixers
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class NpmTsGenTask: DefaultTask() {
    @TaskAction
    fun doAction() {
        project.logger.info("/////////////////////////")
        project.logger.info("TsGenTask")
        project.rootProject.extensions.fixers?.kt2Ts?.let { config ->
            val cleaning = config.buildCleaningRegex()
            project.logger.info("Cleaning: $cleaning")
            project.cleanProject(cleaning)
        }

    }
}