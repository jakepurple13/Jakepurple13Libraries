plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.programmersbox.jakepurple13libraries"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.programmersbox.jakepurple13libraries"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation(libs.bundles.compose)
    implementation("com.github.jakepurple13.Jakepurple13Libraries:patterninput:d89158de3e")
    implementation("com.github.jakepurple13.Jakepurple13Libraries:groupbutton:d89158de3e")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    //androidTestImplementation("androidx.compose.ui:ui-test-junit4:${libs.versions.compose}")
    //debugImplementation("androidx.compose.ui:ui-tooling:${libs.versions.compose}")
    //debugImplementation("androidx.compose.ui:ui-test-manifest:${libs.versions.compose}")
}