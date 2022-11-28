package scripts

//apply(plugin = "com.android.library")
//apply(plugin = "org.jetbrains.kotlin.android")
buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:7.3.1")
    }
}

plugins {
    `kotlin-dsl`
    id("com.android.application") apply false
    id("maven-publish")
}

/*object ArtifactInfo {
    val id = ""
    val version = ""
}*/
apply {
    android {
        publishing {
            singleVariant("release") {
                withSourcesJar()
            }
        }
    }

    publishing {
        publications {
            // Creates a Maven publication called "release".
            register<MavenPublication>("release") {
                // You can then customize attributes of the publication as shown below.
                groupId = "com.github.jakepurple13"
                artifactId = project.ext["id"]//ArtifactInfo.id//"patterninput"
                version = AppInfo.ARTIFACT_VERSION//ArtifactInfo.version//"1.0.0"
                afterEvaluate { from(components["release"]) }
            }
        }
    }
}

//apply(from = rootProject.file("buildSrc/publishing.gradle.kts"))