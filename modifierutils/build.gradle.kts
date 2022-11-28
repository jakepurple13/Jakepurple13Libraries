plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    //id("maven-publish")
}

android {
    namespace = "com.programmersbox.modifierutils"
    compileSdk = 33

    defaultConfig {
        minSdk = 23
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

//    publishing {
//        singleVariant("release") {
//            withSourcesJar()
//        }
//    }
}

/*publishing {
    publications {
        // Creates a Maven publication called "release".
        register<MavenPublication>("release") {
            // You can then customize attributes of the publication as shown below.
            groupId = "com.github.jakepurple13"
            artifactId = "modifierutils"
            version = AppInfo.ARTIFACT_VERSION
            afterEvaluate { from(components["release"]) }
        }
    }
}*/

dependencies {
    implementation(libs.androidCore)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    implementation(libs.bundles.compose)
}