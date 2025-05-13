package com.example.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * 기능(Feature) 모듈을 위한 컨벤션 플러그인
 */
class FeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.logger.lifecycle("🧩 FeatureConventionPlugin applied to: ${target.name}")

        with(target) {
            // 기본 안드로이드 라이브러리 플러그인 적용
            pluginManager.apply("barrion.android.library")

            // Compose가 필요한 경우 주석 해제
            // pluginManager.apply("barrion.android.library.compose")

            // 필요한 추가 플러그인 적용 (예: Navigation SafeArgs)
            pluginManager.apply("androidx.navigation.safeargs.kotlin")

            // 버전 카탈로그 접근
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            // Feature 모듈 공통 의존성 추가
            dependencies {
                // 네비게이션 의존성
                add("implementation", libs.findLibrary("androidx-navigation-fragment-ktx").get())
                add("implementation", libs.findLibrary("androidx-navigation-ui-ktx").get())

                // 공통 프로젝트 의존성 추가
                // 프로젝트 구조에 맞게 수정 필요
                add("implementation", project(":core:common"))
                add("implementation", project(":core:ui"))

                // 도메인 및 데이터 레이어 의존성 (필요한 경우)
                // 프로젝트 구조에 맞게 수정 필요
                // add("implementation", project(":domain"))
                // add("implementation", project(":data"))
            }

            logger.lifecycle("✅ FeatureConventionPlugin successfully applied")
        }
    }
}