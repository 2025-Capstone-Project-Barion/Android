package com.example.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            // 안드로이드 라이브러리에 Compose 설정 적용
            pluginManager.withPlugin("com.android.library") {
                extensions.configure<LibraryExtension> {
                    buildFeatures {
                        compose = true
                    }

                    composeOptions {
                        // 하드코딩된 버전 사용
                        kotlinCompilerExtensionVersion = "1.5.10"
                    }
                }
            }

            // 의존성 추가
            dependencies {
                // 직접 의존성 추가
                add("implementation", "androidx.compose.material3:material3")
                add("implementation", "androidx.compose.ui:ui")
                add("implementation", "androidx.compose.ui:ui-graphics")
                add("implementation", "androidx.compose.ui:ui-tooling-preview")
                add("implementation", "androidx.lifecycle:lifecycle-runtime-compose")
                add("implementation", "androidx.lifecycle:lifecycle-viewmodel-compose")
                add("implementation", "androidx.compose.runtime:runtime")
                add("implementation", "androidx.compose.foundation:foundation")
                add("implementation", platform("androidx.compose:compose-bom:2025.05.00"))

                // 디버그 의존성
                add("debugImplementation", "androidx.compose.ui:ui-tooling")
                add("debugImplementation", "androidx.compose.ui:ui-test-manifest")

                // 테스트 의존성
                add("androidTestImplementation", platform("androidx.compose:compose-bom:2025.05.00"))
                add("androidTestImplementation", "androidx.compose.ui:ui-test-junit4")
            }
        }
    }
}