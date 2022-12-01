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

dependencies {
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    //val purpleLibVersion = "1a941628f0" //This is the first release with the concentrated gradle stuff
    when (AppInfo.location) {
        LibLocation.Individual -> {
            val purpleLibVersion = "1.0.4"
            implementation("com.github.jakepurple13.Jakepurple13Libraries:patterninput:$purpleLibVersion")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:groupbutton:$purpleLibVersion")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:diamondloader:$purpleLibVersion")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:modifierutils:$purpleLibVersion")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:cards:$purpleLibVersion")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:navigationcomposeutils:$purpleLibVersion")
        }
        LibLocation.Bom -> {
            val purpleLibVersion = "1.0.4"
            implementation(platform("com.github.jakepurple13.Jakepurple13Libraries:libraries-bom:$purpleLibVersion"))
            implementation("com.github.jakepurple13.Jakepurple13Libraries:patterninput")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:groupbutton")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:diamondloader")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:modifierutils")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:cards")
            implementation("com.github.jakepurple13.Jakepurple13Libraries:navigationcomposeutils")
        }
        LibLocation.Project -> {
            implementation(projects.diamondloader)
            implementation(projects.cards)
            implementation(projects.modifierutils)
            implementation(projects.patterninput)
            implementation(projects.groupbutton)
            implementation(projects.navigationcomposeutils)
        }
    }

    implementation(libs.navCompose)
    implementation(libs.bundles.composeAll)
    implementation("io.github.oleksandrbalan:pagecurl:1.1.0")
}