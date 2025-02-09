plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("androidx.navigation.safeargs.kotlin")
    id ("dagger.hilt.android.plugin")
    id ("kotlin-kapt")
}

android {
    namespace = "com.example.hospitalapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.hospitalapplication"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    viewBinding.isEnabled = true
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-cast-framework:22.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    // navigation
    val nav_version = "2.6.0"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    // ViewModel
    val lifecycle_version = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    //Retrofit https://square.github.io/retrofit/ - latest vesion https://github.com/square/retrofit.
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    //Gson -> json data to java or kotlin format
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    // coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    // hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    val okhttp3Version = "4.5.0"
    implementation ("com.squareup.okhttp3:logging-interceptor:$okhttp3Version")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
}
kapt {
    correctErrorTypes = true
}