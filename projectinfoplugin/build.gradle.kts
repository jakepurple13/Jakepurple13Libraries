plugins {
    kotlin("jvm")
    alias(libs.plugins.publish)
    id("maven-publish")
}

sourceSets.configureEach {
    java.srcDirs("src/$name/kotlin")
}

val versionInfo = getVersionFromAppInfo() ?: "1.0.9".also { println("Could not find AppInfo") }

group = "io.github.jakepurple13.ProjectInfo"
version = versionInfo

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation("com.jakewharton.picnic:picnic:0.6.0")
    api("com.github.ajalt.mordant:mordant:2.0.0-beta9")
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

fun getVersionFromAppInfo(): String? {
    val regex = Regex("const val ARTIFACT_VERSION = \"(.*?)\"")
    return File("${rootDir.parent}/buildSrc/src/main/kotlin/AppInfo.kt").readLines()
        .firstOrNull { regex.containsMatchIn(it) }
        ?.let { regex.find(it)?.groups?.get(1)?.value }
        ?.also { println("Found AppInfo Version: $it") }
}