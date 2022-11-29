plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "com.programmersbox.diamondloader"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

configurePublishing("diamondloader")

dependencies {

}