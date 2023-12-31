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
            abiFilters += listOf("armeabi", "armeabi-v7a", "arm64-v8a")
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

    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // viewbinding-ktx
    implementation("com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-ktx:2.1.0")
    implementation("com.github.DylanCaiCoding.ViewBindingKTX:viewbinding-base:2.1.0")

    // baidu map
    implementation("com.baidu.lbsyun:BaiduMapSDK_Map-BWNavi:7.5.8")
    implementation("com.baidu.lbsyun:BaiduMapSDK_Search:7.5.8")
    implementation("com.baidu.lbsyun:BaiduMapSDK_Util:7.5.8")
    implementation("com.baidu.lbsyun:BaiduMapSDK_Location:9.3.7")


    // dialog
    implementation("com.afollestad.material-dialogs:core:3.3.0")
    implementation("com.afollestad.material-dialogs:bottomsheets:3.3.0")
    implementation("com.afollestad.material-dialogs:input:3.3.0")

    // liangjingkanji
    implementation("com.github.liangjingkanji:BRV:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("com.github.liangjingkanji:Channel:1.1.5")
    implementation("com.github.liangjingkanji:StatusBar:2.0.5")

    // RWidgetHelper
    implementation("com.github.RuffianZhong:RWidgetHelper:androidx.v0.0.14")

    // splitties
    implementation("com.louiscad.splitties:splitties-fun-pack-android-base:3.0.0")

    // rxjava
    implementation("io.reactivex.rxjava3:rxjava:3.1.6")

    // iconics
    implementation("com.mikepenz:iconics-core:5.4.0")
    implementation("com.mikepenz:iconics-views:5.4.0")
    implementation("com.mikepenz:google-material-typeface:4.0.0.2-kotlin@aar")
    implementation("com.mikepenz:fontawesome-typeface:5.9.0.2-kotlin@aar")
    implementation("com.mikepenz:ionicons-typeface:2.0.1.7-kotlin@aar")

    implementation("com.blankj:utilcodex:1.31.1")
    implementation("com.github.tbruyelle:rxpermissions:0.12")


    implementation("me.majiajie:pager-bottom-tab-strip:2.4.0")

    implementation("me.saket.cascade:cascade:2.2.0")

    implementation("io.github.lucksiege:pictureselector:v3.11.1")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation(files("libs/ocrsdk.aar"))
    implementation(files("libs/ocr_ui-debug.aar"))
    implementation(
        fileTree(
            mapOf(
                "dir" to "libs",
                "include" to listOf("*.aar", "*.jar"),
            )
        )
    )

    implementation("com.github.castorflex.smoothprogressbar:library:1.1.0")

//    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.10")

//    implementation(files('libs/ocrsdk.aar'))
//    implementation files('libs/ocr_ui-debug.aar')

}


