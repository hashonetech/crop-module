@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    id ("kotlin-parcelize")
    id ("maven-publish")
}

group = "com.hashone"
version = "1.0.0"

android {
    namespace = "com.hashone.cropper"
    compileSdk = 33

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        vectorDrawables {
            useSupportLibrary = true
        }
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
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation (libs.compose.extended.gestures)

    // Jetpack Compose
    implementation (libs.androidx.ui)
    implementation (libs.androidx.runtime)

    // Material Design 3 for Compose
    implementation (libs.androidx.material3)

    // Material design icons
    implementation (libs.androidx.material.icons.extended)

    androidTestImplementation (libs.androidx.ui.test.junit4)
    androidTestImplementation(platform(libs.compose.bom))
    debugImplementation (libs.androidx.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
    implementation (libs.glide)
    implementation (libs.accompanist.systemuicontroller)
    api (libs.commons)

}


afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "com.hashone"
                artifactId = "crop"
                version = "1.0.0"

                afterEvaluate {
                    from(components["release"])
                }
            }
        }
    }
}
