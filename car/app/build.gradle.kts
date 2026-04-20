plugins {
    alias(libs.plugins.android.application)
}


android {
    namespace = "com.hls"
    compileSdk {
        version = release(36)
    }
    buildFeatures {
        dataBinding=true
    }

    defaultConfig {
        applicationId = "com.hls"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(files("src\\main\\java\\com\\hls\\lib\\CH34XUartDriver.jar"))
    implementation(libs.core.ktx)
    implementation(libs.ui)
    implementation(libs.coordinatorlayout)
    implementation(libs.legacy.support.v4)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    testCompileOnly("org.projectlombok:lombok:1.18.32")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.32")
    implementation("net.java.dev.jna:jna:5.13.0@aar")
    implementation("com.alphacephei:vosk-android:0.3.75@aar")
    implementation("com.alibaba:fastjson:1.2.83")
//    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")

    implementation("androidx.navigation:navigation-fragment:2.3.2")
    implementation("androidx.navigation:navigation-ui:2.3.2")

    implementation("com.google.android.material:material:1.11.0")

    
    implementation("com.microsoft.onnxruntime:onnxruntime-android:latest.release")
    implementation("com.microsoft.onnxruntime:onnxruntime-extensions-android:latest.release")

}

