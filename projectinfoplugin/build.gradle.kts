plugins {
    kotlin("jvm")
    alias(libs.plugins.publish)
    id("maven-publish")
}

sourceSets.configureEach {
    java.srcDirs("src/$name/kotlin")
}

group = "io.github.jakepurple13.ProjectInfo"
version = "1.0.5"

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
            version = "1.0.5"
            //afterEvaluate { artifact(java.sourceSets.main.get().allSource) }
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
