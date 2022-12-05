plugins {
    kotlin("jvm")
    alias(libs.plugins.publish)
}

sourceSets.configureEach {
    java.srcDirs("src/$name/kotlin")
}

group = "io.github.jakepurple13.ProjectInfo"
version = "0.1.0"

java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    implementation("com.jakewharton.picnic:picnic:0.6.0")
}

gradlePlugin {
    plugins {
        create("plugin") {
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
