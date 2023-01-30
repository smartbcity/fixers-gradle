package city.smartb.fixers.gradle.config

import city.smartb.gradle.config.ConfigExtension
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.invoke


fun Project.configureKt2Ts(mainConfig: ConfigExtension?) {
    val target = this
    mainConfig?.kt2Ts?.let { config ->
        target.tasks {
            register<Delete>("cleanTsGen") {
                delete(config.outputDirectory)
            }

            register<Copy>("tsGen") {
                target.logger.info("*** Start tsGen")
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
                // New
                // ^.*readonly __doNotUseOrImplementIt.*;\n
                // ^.*__doNotUseOrImplementIt:*[\s\S].*\n.*\n.*;
                val cleaning = listOf(
                    ".d.ts" to listOf(
                        Regex("""(?m).*__doNotImplementIt.*\n""") to "",
                        Regex(""".*readonly __doNotUseOrImplementIt.*;\n""") to "",
                        Regex(""".*__doNotUseOrImplementIt:*[\s\S].*\n.*\n.*;""") to "",
                        Regex("""kotlin.js.""") to "",
                        Regex("""org.w3c.dom.url.""") to "",
                        Regex("""org.w3c.dom.""") to "",
                        Regex(""" any/\* ([^*/]*) \*/""") to " $1",
                        Regex("""type Nullable<T> = T \| null \| undefined\n""") to "",
                        Regex("""(?<=\(|, |readonly )(\w*)(\?)?: Nullable<([\w\.<>, ]*)>(?=\)|, |;)""") to "$1?: $3"
                    ),
                    "package.json" to listOf(
                        Regex("""("devDependencies": \{)(.|\n)*?(},)""") to "$1$3"
                    )
                )

                eachFile {
                    cleaning.forEach { (suffix, changes) ->
                        if (file.name.endsWith(suffix)) {
                            val content = file.readText()

                            val newContent = changes.fold(content) { acc, (old, new) ->
                                acc.replace(old, new)
                            }

                            if (newContent != content) {
                                file.writeText(newContent)
                            }
                        }
                    }
                }
            }
        }
    }
}