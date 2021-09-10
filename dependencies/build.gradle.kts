plugins {
    `kotlin-dsl`
}

group = "city.smartb.gradle.dependencies"
version = "SNAPSHOT"

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins.register("dependencies") {
        id = "dependencies"
        implementationClass = "city.smartb.gradle.dependencies.DependenciesPlugin"
    }
}

dependencies {

}