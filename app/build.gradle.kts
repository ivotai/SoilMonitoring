plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {

    namespace = "com.unicorn.soilmonitoring"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.unicorn.soilmonitoring"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        ndk {
            abiFilters += listOf("armeabi", "armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }
    }

    signingConfigs {
        create("unicorn") {
            storeFile = file("C:\\Users\\13276\\AndroidStudioProjects\\SoilMonitoring\\unicorn.jks")
            storePassword = "unicorn"
            keyAlias = "unicorn"
            keyPassword = "unicorn"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("unicorn")
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("unicorn")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding {
        enable = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-ktx:2.1.0")
    implementation("com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-base:2.1.0")

    // BaiduMapSDK
    implementation("com.baidu.lbsyun:BaiduMapSDK_Map:7.5.8")
    implementation("com.baidu.lbsyun:BaiduMapSDK_Search:7.5.8")

}


