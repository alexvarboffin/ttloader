rootProject.name = "ttloader"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()  // Primary repository for dependencies
        google()        // Required for Android-specific dependencies
        gradlePluginPortal()  // Access to Gradle plugins

//        google {
//            mavenContent {
//                includeGroupAndSubgroups("androidx")
//                includeGroupAndSubgroups("com.android")
//                includeGroupAndSubgroups("com.google")
//            }
//        }
        maven("https://maven.google.com")
        maven("https://dl.bintray.com/videolan/Android")

        maven("https://jitpack.io")
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        maven("https://maven.google.com")
        maven("https://dl.bintray.com/videolan/Android")
        mavenCentral()
        maven("https://jitpack.io")
    }
}

include(":app")
include(":ttloader-core")
include(":composeapp")

//include(":medialoot")
//include(":Desktop")
//
include(":features:ui")
project(":features:ui").projectDir = File("/workspace/deps/WalhallaUI/features/ui")

include(":features:wads")
project(":features:wads").projectDir = File("/workspace/deps/WalhallaUI/features/wads")

include(":threader")
project(":threader").projectDir = File("/workspace/deps/multithreader/threader")

//include(":features:permissionResolver")
//project(":features:permissionResolver").projectDir = File("/workspace/deps/WalhallaUI/features/permissionResolver")
//
include(":shared")
project(":shared").projectDir = File("/workspace/deps/WalhallaUI/shared")

////include(":library")
////project(":library").projectDir = File("C:\\Users\\combo\\Desktop\\loader\\youtube-dl-android\\library\\")
//
//include(":service")
//project(":service").projectDir = File("D:\\kwork\\IncomingPhone\\app")


include(":features:permissionResolver")
project(":features:permissionResolver").projectDir = File("/workspace/deps/WalhallaUI/features/permissionResolver")
include(":intentresolver")
