plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    //id("com.apollographql.apollo3") version "3.8.5"
    id("com.diffplug.spotless")
    id("com.google.gms.google-services")
}

android {
    namespace = "net.uoneweb.android.til"
    compileSdk = 35

    defaultConfig {
        applicationId = "net.uoneweb.android.til"
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
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    dataBinding {
        enable = true
    }
}

//apollo {
//    service("service") {
//        packageName.set("com.trevorblades.countries")
//    }
//}

dependencies {
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    val nav_version = "2.8.9" // Compose 1.7.0

    implementation("androidx.fragment:fragment-ktx:1.8.6")
    implementation(libs.androidx.activity.compose)
    implementation("androidx.appcompat:appcompat")

    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.compose.material) // TODO: remove
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation(libs.androidx.ui.tooling.preview)

    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    implementation("androidx.navigation:navigation-compose:$nav_version")

    implementation("com.google.mlkit:barcode-scanning:17.3.0")

    //implementation("com.apollographql.apollo3:apollo-runtime:3.8.5") //TODO: fix

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    val camerax_version = "1.4.2"
    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation("androidx.camera:camera-video:${camerax_version}")
    implementation("androidx.camera:camera-view:${camerax_version}")
    implementation("androidx.camera:camera-extensions:${camerax_version}")

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation(libs.coil.compose)


    val filrebaseBom = platform(libs.firebase.bom)
    implementation(filrebaseBom)

    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-analytics")



    implementation("androidx.credentials:credentials:1.5.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")


    implementation(libs.androidx.datastore.preferences) // 1.1.5は利用不可
    val coroutinesVersion = "1.10.2"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")



    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.google.truth:truth:1.4.2")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("com.google.dagger:hilt-android:2.42")
    kapt("com.google.dagger:hilt-compiler:2.42")


    implementation(project(":gis"))
    implementation(project(":receipt"))

}

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**/*.kt")
        ktlint("1.0.1")
    }
}