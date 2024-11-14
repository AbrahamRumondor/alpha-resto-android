import java.util.Properties
import io.quarkus.gradle.tasks.QuarkusDev
import org.gradle.internal.classpath.Instrumented.systemProperty


plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("com.google.gms.google-services")
  id("com.google.dagger.hilt.android")
  id("androidx.navigation.safeargs.kotlin")
  id("kotlin-kapt")
  id("dagger.hilt.android.plugin")
}


android {
  namespace = "com.example.alfaresto_customersapp"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.example.alfaresto_customersapp"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "com.example.alfaresto_customersapp.HiltTestRunner"

    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
    manifestPlaceholders["location.apikey"] = properties.getProperty("MAPS_API_KEY")
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
  buildFeatures {
    viewBinding = true
    buildConfig = true
  }

  packaging {
    resources.excludes.addAll(
      listOf(
        "META-INF/LICENSE.md",
        "META-INF/LICENSE-notice.md"
      )
    )
  }

  testOptions {
    packagingOptions {
      jniLibs {
        useLegacyPackaging = true // Ensures legacy packaging for jniLibs
      }
    }

    execution = "ANDROIDX_TEST_ORCHESTRATOR"
  }
}

tasks.withType(QuarkusDev::class.java).configureEach {
  jvmArgs.add("-Djdk.attach.allowAttachSelf")
}

kapt {
  correctErrorTypes = true
}

dependencies {

  implementation("com.google.firebase:firebase-messaging:24.0.0")
  implementation("com.google.firebase:firebase-messaging-ktx:24.0.0")
  androidTestImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
  val coroutinesAndroid = "1.7.1"
  val coroutinesCore = "1.6.4"

  implementation("androidx.core:core-ktx:1.13.1")
  implementation("androidx.appcompat:appcompat:1.7.0")
  implementation("com.google.android.material:material:1.12.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
  implementation("androidx.fragment:fragment-ktx:1.8.0")
  implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
  implementation("com.google.firebase:firebase-firestore:25.1.1")
  implementation("com.google.firebase:firebase-auth:23.0.0")
  implementation("com.google.firebase:firebase-auth-ktx:23.0.0")
  implementation("com.google.firebase:firebase-storage:21.0.0")
  implementation("com.google.android.gms:play-services-maps:18.2.0")
  implementation("com.google.firebase:firebase-messaging:24.0.3")
  implementation("com.google.firebase:firebase-functions:21.0.0")
  implementation("com.google.firebase:firebase-messaging-ktx:24.0.3")
  implementation("com.google.firebase:firebase-crashlytics-buildtools:3.0.2")
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

  // paging 3
  implementation("androidx.paging:paging-runtime-ktx:3.3.0")

  // hilt
  implementation("com.google.dagger:hilt-android:2.48")
  kapt("com.google.dagger:hilt-android-compiler:2.48")

  // glide
  implementation("com.github.bumptech.glide:glide:4.12.0")
  kapt("com.github.bumptech.glide:compiler:4.12.0")

  implementation("com.google.android.gms:play-services-maps:18.2.0")
  implementation("androidx.activity:activity-ktx:1.9.0")

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutinesAndroid")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesAndroid")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesCore")

  implementation("androidx.room:room-runtime:2.6.1")
  implementation("androidx.room:room-ktx:2.6.1")
  kapt("androidx.room:room-compiler:2.6.1")

  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

  implementation("com.squareup.okhttp3:okhttp:4.9.3")
  implementation("com.squareup.retrofit2:converter-gson:2.9.0")
  implementation("com.google.maps.android:android-maps-utils:2.2.0")
  implementation("com.google.firebase:firebase-database:21.0.0")

  implementation("com.facebook.shimmer:shimmer:0.5.0")

  // timber
  implementation("com.jakewharton.timber:timber:5.0.1")

  androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
  androidTestImplementation("androidx.test.espresso:espresso-intents:3.6.1")
  androidTestImplementation("androidx.test.espresso:espresso-contrib:3.6.1")

  // JUnit for Espresso tests
  androidTestImplementation("androidx.test.ext:junit:1.2.1")

  // JUnit KTX (check the version compatibility)
  androidTestImplementation("androidx.test.ext:junit-ktx:1.2.1")

  // Other dependencies you might need for testing
  androidTestImplementation("androidx.test:runner:1.6.2")
  implementation("androidx.test:core:1.6.1") // important

  debugImplementation("androidx.fragment:fragment-testing:1.8.5")
  debugImplementation("androidx.fragment:fragment-testing-manifest:1.8.5")

  androidTestImplementation("com.google.dagger:hilt-android-testing:2.51.1")
  kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.51.1")

  testImplementation("androidx.test.espresso:espresso-contrib:3.4.0") {
    exclude(module = "protobuf-lite")
  }

  testImplementation("org.mockito:mockito-core:5.4.0")
  androidTestImplementation("org.mockito:mockito-android:4.0.0")
  androidTestImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")

  androidTestImplementation("androidx.test:rules:1.6.1")
  androidTestImplementation("androidx.test:runner:1.6.2")

  androidTestImplementation("com.google.dagger:hilt-android-testing:2.51.1")
  kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.51.1")


  testImplementation("io.mockk:mockk:1.13.2")  // Use the latest version if available
  androidTestImplementation("io.mockk:mockk-android:1.13.2") // For Android instrumented tests

  androidTestUtil("androidx.test:orchestrator:1.5.1")
}


//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
