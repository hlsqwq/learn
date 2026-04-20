plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.hls.scan"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.hls.scan"
        minSdk = 23
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


// 1. 定义版本号（Kotlin 中使用 val，且注意作用域）
    val cameraXVersion = "1.3.4"

    // 2. CameraX 核心库
    // Kotlin 中必须使用双引号 "" 才能进行 ${} 变量插值
    implementation("androidx.camera:camera-camera2:$cameraXVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraXVersion")
    implementation("androidx.camera:camera-view:$cameraXVersion")

    // 3. Google ML Kit 条码扫描
    implementation("com.google.mlkit:barcode-scanning:17.3.0")
}