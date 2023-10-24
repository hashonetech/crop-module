@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    id ("kotlin-parcelize")
    id ("maven-publish")
}

group = "com.hashone"
version = "1.1.2"

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

    api(libs.core.ktx)
    api(libs.appcompat)
    api(libs.material)
//    implementation(libs.firebase.crashlytics.buildtools)
    api(libs.lifecycle.runtime.ktx)
    api(libs.activity.compose)
    api(platform(libs.compose.bom))
    api(libs.ui.graphics)
    api(libs.ui.tooling.preview)

    api (libs.compose.extended.gestures)

    // Jetpack Compose
    api (libs.androidx.ui)
    api (libs.androidx.runtime)

    // Material Design 3 for Compose
    api (libs.androidx.material3)

    // Material design icons
    api (libs.androidx.material.icons.extended)
    api (libs.glide)
    api (libs.accompanist.systemuicontroller)
    api (libs.commons)

}


afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "com.hashone"
                artifactId = "crop"
                version = "1.1.2"

                afterEvaluate {
                    from(components["release"])
                }
            }
        }
    }
}
