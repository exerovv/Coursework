plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
    alias(libs.plugins.safeargs)
}

android {
    namespace = "com.example.coursework"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.coursework"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures{
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.logging)
    implementation(libs.gson.converter)
    implementation(libs.rxjava.adapter)

    //Glide
    implementation(libs.glide)

    //Paging
    implementation(libs.paging)
    implementation(libs.rxjava.paging)

    //ViewModel and LiveData
    implementation(libs.viewmodel)
    implementation(libs.livedata)

    //RxJava
    implementation(libs.rxjava)
    implementation(libs.rxjava.android)

    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    //Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}