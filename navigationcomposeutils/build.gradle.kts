plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "com.programmersbox.navigationcomposeutils"
}

dependencies {
    implementation(libs.navCompose)
}