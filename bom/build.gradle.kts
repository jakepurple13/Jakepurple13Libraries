plugins {
    //kotlin("jvm")
    id("java-platform")
    id("maven-publish")
    //`kotlin-dsl`
}

dependencies {
    constraints {
        project.rootProject.subprojects.forEach { subproject ->
            if (subproject.name != "app") {
                api(subproject)
            }
        }
    }
}

publishing {

    /*repositories {
        maven(url = "~/.gradle/caches/modules-2/files-2.1")
    }*/

    publications {
        // Create a maven publication called "release"
        register<MavenPublication>("release") {
            groupId = "com.github.jakepurple13"
            artifactId = "libraries-bom"
            version = AppInfo.ARTIFACT_VERSION
            afterEvaluate { from(components["javaPlatform"]) }
        }
    }
}