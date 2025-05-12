package com.example.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

// Const 객체 추가
object Const {
    const val COMPILE_SDK = 35
    const val MIN_SDK = 21
    const val TARGET_SDK = 35
    val JAVA_VERSION = JavaVersion.VERSION_17
}

// KotlinAndroid.kt
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = Const.COMPILE_SDK

        defaultConfig {
            minSdk = Const.MIN_SDK
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables {
                useSupportLibrary = true
            }
        }

        // 수정된 buildTypes 블록
        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }

        compileOptions {
            sourceCompatibility = Const.JAVA_VERSION
            targetCompatibility = Const.JAVA_VERSION
        }

        kotlinOptions {
            jvmTarget = Const.JAVA_VERSION.toString()
        }

        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }
}

internal fun CommonExtension<*, *, *, *, *, *>.kotlinOptions(
    block: KotlinJvmOptions.() -> Unit
) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}