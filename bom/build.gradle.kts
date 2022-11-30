plugins {
    //kotlin("jvm")
    id("java-platform")
    id("maven-publish")
    //`kotlin-dsl`
}

dependencies {
    constraints {
        project.rootProject.subprojects.forEach { subproject ->
            api(projects.groupbutton)
            api(projects.navigationcomposeutils)
            api(projects.diamondloader)
            api(projects.patterninput)
            api(projects.modifierutils)
            api(projects.cards)
        }
    }
}

/*task androidSourcesJar(type: Jar) {
    archiveClassifier.set("sources")
    if (project.plugins.findPlugin("com.android.library")) {
        from android.sourceSets.main.java.srcDirs
                from android.sourceSets.main.kotlin.srcDirs
    } else {
        from sourceSets.main.java.srcDirs
                from sourceSets.main.kotlin.srcDirs
    }
}

task javadocJar(type: Jar, dependsOn: dokkaJavadoc) {
    archiveClassifier.set("javadoc")
    from dokkaJavadoc.outputDirectory
}

artifacts {
    archives androidSourcesJar
            archives javadocJar
}*/

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