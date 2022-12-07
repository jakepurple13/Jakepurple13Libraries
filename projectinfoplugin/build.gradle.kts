plugins {
    kotlin("jvm")
    alias(libs.plugins.publish)
    id("maven-publish")
}

sourceSets.configureEach {
    java.srcDirs("src/$name/kotlin")
}

val versionInfo = "1.0.7"

group = "io.github.jakepurple13.ProjectInfo"
version = versionInfo

java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    implementation("com.jakewharton.picnic:picnic:0.6.0")
    implementation(gradleApi())
    implementation(kotlin("stdlib"))
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            // You can then customize attributes of the publication as shown below.
            groupId = "com.github.jakepurple13"
            artifactId = "projectInfo"
            version = versionInfo
        }
    }
}

gradlePlugin {
    plugins {
        create("projectInfoPlugin") {
            id = group.toString()
            displayName = "Project Info"
            description = "Allows you to view and see project info"
            implementationClass = "com.programmersbox.ProjectInfoPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/jakepurple13/Jakepurple13Libraries"
    vcsUrl = "https://github.com/jakepurple13/Jakepurple13Libraries"
    tags = listOf("stats")
}

tasks.create("setupPluginUploadFromEnvironment") {
    doLast {
        val key = System.getenv("GRADLE_PUBLISH_KEY")
        val secret = System.getenv("GRADLE_PUBLISH_SECRET")

        if (key == null || secret == null) {
            throw GradleException("gradlePublishKey and/or gradlePublishSecret are not defined environment variables")
        }

        System.setProperty("gradle.publish.key", key)
        System.setProperty("gradle.publish.secret", secret)
    }
}