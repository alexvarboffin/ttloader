import java.text.SimpleDateFormat
import java.util.Date

fun versionCodeDate(): Int {
    val dateFormat = SimpleDateFormat("yyMMdd")
    return dateFormat.format(Date()).toInt()
}
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.walhalla.ttvloader"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.android.buildTools.get()

    val versionPropsFile = file("version.properties")

    if (versionPropsFile.canRead()) {
        val code = versionCodeDate()

        defaultConfig {
            resConfigs("en", "es", "fr", "de", "it", "pt", "el", "ru", "ja", "zh-rCN", "zh-rTW", "ko", "ar", "uk", "vi", "uz", "az")
            multiDexEnabled = true
            applicationId = "com.walhalla.ttloader"
            minSdk = libs.versions.android.minSdk.get().toInt()
            targetSdk = libs.versions.android.targetSdk.get().toInt()
            versionCode = code
            versionName = "1.1.$code"
            setProperty("archivesBaseName", "com.Walhalla.TTLoader")
        }
    } else {
        throw GradleException("Could not read version.properties!")
    }

    signingConfigs {

        create("x") {
            keyAlias = "tiktokdwn"
            keyPassword = "@!sfuQ123zpc"
            storeFile = file("keystore/keystore.jks")
            storePassword = "@!sfuQ123zpc"
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            versionNameSuffix = "-DEMO"
            signingConfig = signingConfigs.getByName("x")
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("x")
            versionNameSuffix = ".release"
        }
    }

    useLibrary("org.apache.http.legacy")

    flavorDimensions += "W"
    productFlavors {
        create("ttloader") {
            dimension = "W"
            applicationId = "com.walhalla.ttloader"
            setProperty("archivesBaseName", "Ttloader")
            signingConfig = signingConfigs.getByName("x")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

//    lint {
//        isAbortOnError = false
//        isCheckReleaseBuilds = false
//    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

repositories {
    flatDir {
        dirs("libs")
    }
}

tasks.register<Copy>("copyAabToBuildFolder") {
    println("mmmmmmmmmmmmmmmmm ${layout.buildDirectory.get()}/outputs/bundle/release")
    println("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm")
    val outputDirectory = file("C:/build")
    if (!outputDirectory.exists()) {
        outputDirectory.mkdirs()
    }

    from("${layout.buildDirectory.get()}/outputs/bundle/release") {
        include("*.aab")
    }
    into(outputDirectory)
}

apply(from = "C:\\scripts/copyReports.gradle")

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    //implementation(name = "toasty-production-release", ext = "aar")

    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    //implementation(libs.androidx.cardview)
    implementation("androidx.cardview:cardview:1.0.0")
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    implementation(libs.picasso)
    implementation(libs.circleimageview)
    implementation(libs.jsoup)
    implementation(libs.glide)
    implementation(project(":intentresolver"))
    implementation(project(":features:wads"))
    annotationProcessor(libs.glide.compiler)
    implementation(libs.play.services.ads)
    implementation(libs.androidx.recyclerview)
    implementation(project(":features:permissionResolver"))
    implementation(project(":features:ui"))
    implementation(project(":threader"))

    implementation(libs.androidx.multidex)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.preference.ktx)

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlin.stdlib)

    implementation(libs.apache.commons.lang3)
//    implementation(libs.media3.exoplayer)
//    implementation(libs.media3.ui)
//    implementation(libs.media3.exoplayer.dash)

    implementation(libs.sdp.android)
    implementation(libs.logging.interceptor)

    implementation(libs.filepicker)
    implementation(libs.konfetti.xml)
}