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
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    val purpleLibVersion = "1.0.0"
    //val purpleLibVersion = "1a941628f0"
    implementation("com.github.jakepurple13.Jakepurple13Libraries:patterninput:$purpleLibVersion")
    implementation("com.github.jakepurple13.Jakepurple13Libraries:groupbutton:$purpleLibVersion")
    implementation("com.github.jakepurple13.Jakepurple13Libraries:diamondloader:$purpleLibVersion")
    implementation("com.github.jakepurple13.Jakepurple13Libraries:modifierutils:$purpleLibVersion")
    //implementation(projects.diamondloader)
    implementation(libs.navCompose)
    //androidTestImplementation("androidx.compose.ui:ui-test-junit4:${libs.versions.compose}")
    //debugImplementation("androidx.compose.ui:ui-tooling:${libs.versions.compose}")
    //debugImplementation("androidx.compose.ui:ui-test-manifest:${libs.versions.compose}")
}