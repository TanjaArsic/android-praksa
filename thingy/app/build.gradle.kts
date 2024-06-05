import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

//import org.jetbrains.kotlin.kapt3.base.Kapt.kapt


plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.thingy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.thingy"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    dependencies {
        val daggerVersion = "2.49"
        val hiltVersion = "1.2.0-alpha01"

        implementation("androidx.core:core-ktx:1.9.0")
        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("com.google.android.material:material:1.11.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.compose.ui:ui-tooling-preview-android:1.6.4")
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
        // Retrofit
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("com.squareup.okhttp3:okhttp:4.9.0")
        implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
        // Coil
        implementation("io.coil-kt:coil-compose:1.3.2")
        // Coroutine Lifecycle Scopes
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
        implementation("androidx.compose.material3:material3:1.0.1")

        implementation("androidx.fragment:fragment-ktx:1.6.2")
        implementation("androidx.activity:activity-compose:1.3.0-alpha07")
        implementation("androidx.compose.runtime:runtime:1.3.2")
        implementation("androidx.compose.runtime:runtime-livedata:1.3.2")


        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")
        implementation("androidx.navigation:navigation-compose:2.7.7")

        //Hilt
        implementation("com.google.dagger:hilt-android:$daggerVersion")
        kapt("com.google.dagger:hilt-android-compiler:$daggerVersion")
        implementation("androidx.hilt:hilt-navigation-compose:$hiltVersion")


        //Maps
        implementation("com.google.maps.android:android-maps-utils:3.8.0")
        implementation("com.google.android.gms:play-services-maps:18.0.2")


    }
}
