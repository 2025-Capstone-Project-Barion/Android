package com.example.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * 안드로이드 라이브러리 모듈을 위한 컨벤션 플러그인
 */
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.logger.lifecycle("📚 ===============================================")
        target.logger.lifecycle("📚 AndroidLibraryConventionPlugin applied to: ${target.name}")
        target.logger.lifecycle("📚 ===============================================")

        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")

                // 각 플러그인이 적용되었는지 로그로 확인
                logger.lifecycle("✅ Applied: com.android.library")
                logger.lifecycle("✅ Applied: org.jetbrains.kotlin.android")
            }

            // 버전 카탈로그에 접근
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            logger.lifecycle("📚 Accessed version catalog: libs")

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                logger.lifecycle("⚙️ Configured Kotlin Android settings")

                defaultConfig {
                    minSdk = Const.MIN_SDK
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }
                logger.lifecycle("📱 Set library default config")

                buildFeatures {
                    viewBinding = true
                    dataBinding = true
                    buildConfig = true
                }
                logger.lifecycle("🛠️ Configured build features")
            }

            // 의존성 추가 시 확인 로그
            logger.lifecycle("📦 Adding core dependencies...")

            // 라이브러리 모듈 핵심 의존성 추가
            dependencies {
                // 기본 안드로이드 의존성만 유지
                add("implementation", libs.findLibrary("androidx-core-ktx").get())
                add("implementation", libs.findLibrary("androidx-appcompat").get())
                add("implementation", libs.findLibrary("material").get())

                // 테스트 의존성
                add("testImplementation", libs.findLibrary("junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx-junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx-espresso-core").get())
            }

            // 플러그인 적용 완료 메시지
            logger.lifecycle("✅ ===============================================")
            logger.lifecycle("✅ AndroidLibraryConvention successfully applied")
            logger.lifecycle("✅ ===============================================")
        }
    }
}