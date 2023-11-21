plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("kotlin-android")
    id("kotlin-kapt")
//    id("kotlinx-serialization")
    id("kotlin-parcelize")
}

android {
    namespace = "com.nabin0.audio_player"
    compileSdk = 34

    defaultConfig {
        minSdk = 22

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    //androidTestImplementation 'org.mockito:mockito-core:4.2.0'
    //androidTestImplementation 'org.mockito:mockito-android:4.6.1'
//    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    //   androidTestImplementation 'org.mockito:mockito-inline:3.4.0'
    // androidTestImplementation "com.linkedin.dexmaker:dexmaker-mockito-inline-extended:2.28.1"
    //   testImplementation('org.mockito:mockito-inline:3.4.0')
    //   testImplementation 'org.powermock:powermock:1.6.5'
    //   testImplementation 'org.powermock:powermock-module-junit4:1.6.5'
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")
    testImplementation ("org.robolectric:robolectric:4.8.1")
//    androidTestImplementation("org.robolectric:robolectric:4.8.1")
    testImplementation ("androidx.test:core:1.5.0")
    testImplementation ("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    testImplementation ("io.mockk:mockk:1.13.3")
    androidTestImplementation ("io.mockk:mockk-android:1.13.3")
    androidTestImplementation("io.mockk:mockk-agent:1.13.3")
    testImplementation ("io.mockk:mockk-android:1.13.3")
    testImplementation ("io.mockk:mockk-agent:1.13.3")
    androidTestImplementation ("androidx.test.ext:junit-ktx:1.1.5")
    implementation ("com.squareup.retrofit2:retrofit-mock:2.9.0")

    implementation("androidx.media3:media3-exoplayer-ima:1.2.0")
    implementation("androidx.media3:media3-exoplayer:1.2.0")
//    implementation("androidx.media3:media3-exoplayer-dash:1.2.0")
//    implementation("androidx.media3:media3-exoplayer-smoothstreaming:1.1.0")
//    implementation("androidx.media3:media3-exoplayer-hls:1.1.0")
    implementation("androidx.media3:media3-ui:1.2.0")
//    implementation("androidx.media3:media3-cast:1.1.0")
    implementation ("androidx.media3:media3-session:1.2.0")

    implementation("androidx.mediarouter:mediarouter:1.6.0")
    implementation("com.google.android.gms:play-services-cast-framework:21.3.0")
}