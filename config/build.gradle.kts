plugins {
    `kotlin-dsl`
    `maven-publish`
    signing
}

repositories {
    mavenCentral()
}

group = "city.smartb.fixers"

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(project.components["kotlin"])
            pom {
                name.set(project.name)
                description.set(project.description)

                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("SmartB")
                        name.set("SmartB Team")
                        organization.set("SmartB")
                        organizationUrl.set("https://www.smartb.city")
                    }
                }
            }
        }
    }
}