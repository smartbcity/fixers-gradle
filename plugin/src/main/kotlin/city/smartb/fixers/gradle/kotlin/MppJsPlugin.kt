package city.smartb.fixers.gradle.kotlin

import city.smartb.fixers.gradle.dependencies.PluginVersions
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@Suppress("UnstableApiUsage")
class MppJsPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        configureJsCompilation(target)
    }

    private fun configureJsCompilation(target: Project) {
        target.extensions.configure(KotlinMultiplatformExtension::class.java) {
            js(IR) {
                binaries.library()
                browser {
                    browser()
                    binaries.executable()

                    testTask {
                        useKarma {
                            useChromeHeadless()
                        }
                    }
                }
            }
            sourceSets.getByName("jsMain") {
                dependencies {
                }
            }
            sourceSets.getByName("jsTest") {
                dependencies {
                    implementation("org.jetbrains.kotlin:kotlin-test-js:${PluginVersions.kotlin}")
                }
            }
        }
    }
}
