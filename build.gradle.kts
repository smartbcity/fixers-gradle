plugins {
	kotlin("jvm") version PluginVersions.kotlin apply false
	id("com.gradle.plugin-publish") version PluginVersions.gradlePublish apply false
}


allprojects {
	version = System.getenv("VERSION") ?: "latest"
	repositories {
		mavenCentral()
		gradlePluginPortal()
	}
}

subprojects {
	plugins.withType(PublishingPlugin::class.java).whenPluginAdded {
		plugins.withType(SigningPlugin::class.java).whenPluginAdded {
			extensions.getByType(SigningExtension::class.java).apply {
				val inMemoryKey = System.getenv("signingKey") ?: findProperty("signingKey")?.toString()
				val password = System.getenv("signingPassword") ?: findProperty("signingPassword")?.toString()
				if (inMemoryKey != null) {
					isRequired = true
					useInMemoryPgpKeys(inMemoryKey, password)
					sign(
						extensions.getByType(PublishingExtension::class.java).publications
					)
				}
			}
		}
		extensions.getByType(PublishingExtension::class.java).apply {
			val sonatypeUrl = if(System.getenv("VERSION").endsWith("-SNAPSHOT")) {
				"https://oss.sonatype.org/content/repositories/snapshots/"
			} else {
				"https://oss.sonatype.org/service/local/staging/deploy/maven2"
			}
			repositories {
				maven {
					name = "sonatype"
					url = uri(sonatypeUrl)
					credentials {
						username = System.getenv("sonatypeUsername")
						password = System.getenv("sonatypePassword")
					}
				}
			}
		}
	}

}
