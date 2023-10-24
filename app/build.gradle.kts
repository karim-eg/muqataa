/*
 * Copyright (c) 2023-2023. Encept Ltd Company, https://encept.co
 * contact: support@encept.co
 */

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("C:\\Users\\Administrator\\Android_Sign_Keys\\Muqataa\\muqataa.jks")
            storePassword = "EeNnCcEePpTt@123"
            keyAlias = "EeNnCcEePpTt@123"
            keyPassword = "EeNnCcEePpTt@123"
        }
    }
    namespace = "co.encept.muqataa"
    compileSdk = 34

    defaultConfig {
        applicationId = "co.encept.muqataa"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation ("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("com.google.android.material:material:1.10.0")
    implementation ("com.google.android.gms:play-services-ads:22.4.0")

    // ML Kit
    implementation ( "com.google.mlkit:barcode-scanning:17.2.0")

    // CameraX core library using camera2 implementation
    implementation ("androidx.camera:camera-core:1.4.0-alpha02")
    implementation ("androidx.camera:camera-camera2:1.4.0-alpha02")
    implementation ("androidx.camera:camera-lifecycle:1.4.0-alpha02")
    implementation ("androidx.camera:camera-view:1.4.0-alpha02")

    implementation ("androidx.core:core-splashscreen:1.0.1")

    implementation ("de.hdodenhof:circleimageview:3.1.0")

    // okhttp network request
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    implementation ("com.google.guava:guava:32.1.3-jre")
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.0.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}