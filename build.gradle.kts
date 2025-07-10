plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlin.android) apply false

    kotlin("plugin.serialization") version "1.9.0" apply false

    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room).apply(false)
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.google.firebase.crashlytics) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    //kotlin("native.cocoapods") apply false


    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}