
println(
  """
TOOL VERSIONS
  JDK: ${System.getProperty("java.version")}
  Gradle: ${gradle.gradleVersion}
""".trimIndent()
)

plugins {
  id("city.smartb.fixers.gradle.config")
  id("city.smartb.fixers.gradle.publish") apply false
  id("city.smartb.fixers.gradle.sonar")
}

allprojects {
  group = "city.smartb.gradle.sandbox"
  version = System.getenv("VERSION") ?: "latest"
  repositories {
    mavenLocal()
    mavenCentral()
  }
}

//subprojects {
//  plugins.withType(city.smartb.fixers.gradle.config.ConfigPlugin::class.java).whenPluginAdded {
//    fixers {
//      bundle {
//        id = "gradle-sandbox"
//        name = "gradle-sandbox"
//        description = "Sanbox to test SmartB Kotlin Configuration"
//        url = "https://gitlab.smartb.city/fixers/gradle/sandbox"
//      }
//    }
//  }
//}

fixers {
  bundle {
    id = "gradle-sandbox"
    name = "gradle-sandbox"
    description = "Sanbox to test SmartB Kotlin Configuration"
    url = "https://gitlab.smartb.city/fixers/gradle/sandbox"
  }
  kt2Ts {
    outputDirectory = "storybook/d2/"
    inputDirectory = "ef"
  }
}
