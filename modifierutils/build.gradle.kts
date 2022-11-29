plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "com.programmersbox.modifierutils"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
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
            artifactId = "modifierutils"
            version = AppInfo.ARTIFACT_VERSION
            afterEvaluate { from(components["release"]) }
        }
    }
}

dependencies {

}