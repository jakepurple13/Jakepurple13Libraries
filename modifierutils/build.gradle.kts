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
}

configurePublishing("modifierutils")

dependencies {

}