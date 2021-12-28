plugins {
    `kotlin-dsl`
    `maven-publish`
    signing
}

apply(from = rootProject.file("gradle/publishing.gradle"))