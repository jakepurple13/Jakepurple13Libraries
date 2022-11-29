plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "com.programmersbox.patterninput"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

configurePublishing("patterninput")

dependencies {

}