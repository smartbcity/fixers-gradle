package city.smartb.fixers.gradle.config

import city.smartb.gradle.config.ConfigExtension
import city.smartb.gradle.config.model.Kt2Ts
import java.io.File
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.register


fun Project.configureKt2Ts(mainConfig: ConfigExtension?) {
    val target = this
    mainConfig?.kt2Ts?.let { config ->
        target.tasks {
            register<Delete>("cleanTsGen") {
                delete(config.outputDirectory)
            }

            register<Copy>("tsGen") {
                dependsOn("cleanTsGen")

                val inputDir = if(config.inputDirectory != null) {
                    config.inputDirectory!!
                } else {
                    "${this.project.buildDir.absolutePath}/js/packages/".also {
                        target.logger.info("fixers.kt2Ts.inputDirectory is not set. Default value [$it] will be used.")
                    }
                }
                from(inputDir) {
                    exclude("*-test")
                }
                into(config.outputDirectory)
                includeEmptyDirs = false

                val cleaning = config.buildCleaningRegex()

                cleanSubProjects(cleaning)
                eachFile {
                    file.cleanFile(cleaning)
                }
            }
        }
    }
}

fun Project.cleanSubProjects(cleaning: MutableMap<String, List<Pair<Regex, String>>>) {
    project.logger.info("----------------")
    subprojects.forEach { project ->
        project.cleanProject(cleaning)
    }
}

fun Project.cleanProject(cleaning: MutableMap<String, List<Pair<Regex, String>>>) {
    project.logger.info("//////////////////////////")
    project.logger.info("//////////////////////////")
    val folder = "${project.buildDir.absolutePath}/packages/js"
    project.logger.info(folder)
    File(folder).listFiles()?.forEach { file ->
        file.cleanFile(cleaning)
    }
    project.logger.info("//////////////////////////")
}

fun Kt2Ts.buildCleaningRegex(): MutableMap<String, List<Pair<Regex, String>>> {
    val cleaning = mutableMapOf(
        ".d.ts" to listOf(
            Regex("""(?m).*__doNotImplementIt.*\n""") to "",
            Regex(""".*readonly __doNotUseOrImplementIt.*;\n""") to "",
            Regex(""".*__doNotUseOrImplementIt:*[\s\S].*\n.*\n.*;""") to "",
            Regex("""kotlin.js.""") to "",
            Regex("""org.w3c.dom.url.""") to "",
            Regex("""org.w3c.dom.""") to "",
            Regex(""" any/\* ([^*/]*) \*/""") to " $1",
            Regex("""type Nullable<T> = T \| null \| undefined\n""") to "",
            Regex("""(?<=\(|, |readonly )(\w*)(\?)?: Nullable<([\w\.<>, \[\]]*)>(?=\)|, |;|/*)""") to "$1?: $3",
            Regex("""kotlin.collections.Map""") to "Record",
            Regex("""kotlin\.collections\.List<(.*?>?)>""") to "$1[]",
            Regex("""kotlin\.collections\.List<(.*?>?)>""") to "$1[]", // in case of List<List<T>>
            Regex("""kotlin.Long""") to "number",
            Regex("""static get Companion(.*\n)*?(\s)*}( &.*)?;""") to ""
        ) + additionalCleaning[".d.ts"].orEmpty(),
        "package.json" to listOf(
            Regex("""("devDependencies": \{)(.|\n)*?(},)""") to "$1$3"
        ) + additionalCleaning["package.json"].orEmpty()
    )
    cleaning.putAll(additionalCleaning.filterKeys { it !in cleaning.keys })
    return cleaning
}

private fun File.cleanFile(cleaning: Map<String, List<Pair<Regex, String>>>) {
    cleaning.forEach { (suffix, changes) ->
        if (name.endsWith(suffix)) {
            val content = readText()

            val newContent = changes.fold(content) { acc, (old, new) ->
                acc.replace(old, new)
            }

            if (newContent != content) {
                writeText(newContent)
            }
        }
    }
}
