plugins {
    id("com.google.gms.google-services")
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.pregnancyapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.pregnancyapp"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Use Firebase BOM - only one version here
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))

    // Firebase libraries without explicit versions
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database-ktx")

    // Google Play Services Auth (you can upgrade the version if needed)
    implementation("com.google.android.gms:play-services-auth:21.3.0")

    // AndroidX & Material Design libraries (make sure libs.* are correctly set in your version catalog)
    implementation(libs.appcompat)
    implementation(libs.material)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
