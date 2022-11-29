// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.3.0" apply false
    id("com.android.library") version "7.3.0" apply false
    kotlin("android") version libs.versions.kotlin.get() apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath(libs.androidGradle)
        classpath(libs.kotlinGradle)
    }
}

subprojects {
    afterEvaluate {
        configureKotlinCompile()
        configureAndroidBasePlugin()
    }
}

fun Project.configureKotlinCompile() {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

fun Project.configureAndroidBasePlugin() {
    extensions.findByType<com.android.build.gradle.BaseExtension>()?.apply {
        compileSdkVersion(33)
        defaultConfig {
            minSdk = 23
            targetSdk = 33
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        compileOptions {
            isCoreLibraryDesugaringEnabled = true
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        composeOptions {
            kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
        }
        packagingOptions {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}:"
            }
        }
        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro",
                )
            }
        }

        if (plugins.findPlugin(MavenPublishPlugin::class) != null) setupPublishing()

        buildFeatures.compose = true
        dependencies {
            val coreLibraryDesugaring by configurations
            coreLibraryDesugaring(libs.coreLibraryDesugaring)
            "implementation"(libs.androidCore)
            "implementation"(libs.bundles.compose)
            "testImplementation"("junit:junit:4.13.2")
            "androidTestImplementation"("androidx.test.ext:junit:1.1.4")
            "androidTestImplementation"("androidx.test.espresso:espresso-core:3.5.0")
        }
    }
}

fun Project.setupPublishing(): Unit = (this as ExtensionAware).extensions.configure(
    "android",
    Action<com.android.build.api.dsl.LibraryExtension> {
        publishing {
            singleVariant("release") {
                withSourcesJar()
            }
        }
    }
)