plugins {
    //kotlin("jvm")
    `java-platform`
    `maven-publish`
    //`kotlin-dsl`
}

javaPlatform {
    allowDependencies()
}

dependencies {
    constraints {
        project.rootProject.subprojects
            .filter { it != projects.app }
            .forEach { subproject -> api("com.github.jakepurple13.Jakepurple13Libraries:${subproject.name}:${AppInfo.ARTIFACT_VERSION}") }
        /*api(projects.groupbutton)
        api(projects.navigationcomposeutils)
        api(projects.diamondloader)
        api(projects.patterninput)
        api(projects.modifierutils)
        api(projects.cards)*/
        /*val purpleLibVersion = "1.0.3"
        api("com.github.jakepurple13.Jakepurple13Libraries:patterninput:$purpleLibVersion")
        api("com.github.jakepurple13.Jakepurple13Libraries:groupbutton:$purpleLibVersion")
        api("com.github.jakepurple13.Jakepurple13Libraries:diamondloader:$purpleLibVersion")
        api("com.github.jakepurple13.Jakepurple13Libraries:modifierutils:$purpleLibVersion")
        api("com.github.jakepurple13.Jakepurple13Libraries:cards:$purpleLibVersion")
        api("com.github.jakepurple13.Jakepurple13Libraries:navigationcomposeutils:$purpleLibVersion")*/
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
            from(components["javaPlatform"])
        }
    }
}