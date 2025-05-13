package com.example.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * Hilt 의존성 주입을 위한 컨벤션 플러그인
 */
class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        // 로그 출력
        target.logger.lifecycle("💉 ===============================================")
        target.logger.lifecycle("💉 HiltConventionPlugin applied to: ${target.name}")
        target.logger.lifecycle("💉 ===============================================")

        with(target) {
            // 플러그인 적용
            with(pluginManager) {
                // 먼저 KSP 플러그인 적용 (중요: ksp 설정을 사용하기 전에 플러그인 먼저 적용)
                apply("com.google.devtools.ksp")
                apply("com.google.dagger.hilt.android")
                apply("org.jetbrains.kotlin.kapt")

                logger.lifecycle("✅ Applied: com.google.devtools.ksp")
                logger.lifecycle("✅ Applied: com.google.dagger.hilt.android")
                logger.lifecycle("✅ Applied: org.jetbrains.kotlin.kapt")
            }

            // 버전 카탈로그 접근
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            // Hilt 의존성 추가
            logger.lifecycle("📦 Adding Hilt dependencies...")

            dependencies {
                add("implementation", libs.findLibrary("hilt-android").get())

                // Hilt에는 kapt와 ksp 둘 다 사용 가능, 둘 중 하나만 선택하는 것이 좋음
                // 여기서는 ksp를 우선 사용하고, 문제 시 kapt로 전환
                add("ksp", libs.findLibrary("hilt-compiler").get())
                add("ksp", libs.findLibrary("hilt-android-compiler").get())

                // kapt는 주석 처리 (필요시 활성화)
                // add("kapt", libs.findLibrary("hilt-compiler").get())
                // add("kapt", libs.findLibrary("hilt-android-compiler").get())
            }

            logger.lifecycle("✅ ===============================================")
            logger.lifecycle("✅ HiltConventionPlugin successfully applied")
            logger.lifecycle("✅ ===============================================")
        }
    }
}