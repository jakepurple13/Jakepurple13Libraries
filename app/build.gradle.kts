plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.programmersbox.jakepurple13libraries"

    defaultConfig {
        applicationId = "com.programmersbox.jakepurple13libraries"
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }
}

enum class LibLocation { Individual, Bom, Project }

dependencies {
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    //val purpleLibVersion = "1a941628f0" //This is the first release with the concentrated gradle stuff
    when (LibLocation.Project) {
        LibLocation.Individual -> {
            val purpleLibVersion = "1.0.8"
            implementation("com.github.jakepurple13.Jakepurple13Libraries:patterninput:$purpleLibVersion")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:groupbutton:$purpleLibVersion")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:diamondloader:$purpleLibVersion")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:modifierutils:$purpleLibVersion")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:cards:$purpleLibVersion")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:navigationcomposeutils:$purpleLibVersion")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:randomutils:$purpleLibVersion")
        }
        LibLocation.Bom -> {
            val purpleLibVersion = "1.0.8"
            implementation(platform("com.github.jakepurple13.Jakepurple13Libraries:libraries-bom:$purpleLibVersion"))
            implementation("com.github.jakepurple13.Jakepurple13Libraries:patterninput")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:groupbutton")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:diamondloader")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:modifierutils")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:cards")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:navigationcomposeutils")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:randomutils")
        }
        LibLocation.Project -> {
            implementation(projects.diamondloader)
            implementation(projects.cards)
            implementation(projects.modifierutils)
            implementation(projects.patterninput)
            implementation(projects.groupbutton)
            implementation(projects.navigationcomposeutils)
            implementation(projects.randomutils)
        }
    }

    implementation(libs.navCompose)
    implementation(libs.bundles.composeAll)
    implementation("io.github.oleksandrbalan:pagecurl:1.1.0")
    testImplementation("com.jakewharton.picnic:picnic:0.6.0")
}