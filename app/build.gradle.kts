plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
    namespace = "com.fluidsimulation"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.fluidsimulation"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        ndk {
            abiFilters.add("armeabi-v7a")
            abiFilters.add("arm64-v8a")
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        buildConfig = true
    }
    viewBinding.isEnabled = true
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
    sourceSets {
        named("main") {
            java.srcDir("src/main/jniLibs")
        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.sdp.android)
    implementation(libs.gson)
    implementation(libs.preference)

    implementation(libs.bundles.glide)
    implementation(libs.jp.wasabeef.recyclerview.animators)
    implementation(libs.jp.wasabeef.glide.transformations)
}