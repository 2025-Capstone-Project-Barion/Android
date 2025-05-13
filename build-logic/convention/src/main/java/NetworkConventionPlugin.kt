package com.example.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * 네트워크 통신을 위한 컨벤션 플러그인
 */
class NetworkConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        // 로그 출력
        target.logger.lifecycle("🌐 ===============================================")
        target.logger.lifecycle("🌐 NetworkConventionPlugin applied to: ${target.name}")
        target.logger.lifecycle("🌐 ===============================================")

        with(target) {
            // 플러그인 적용
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
            logger.lifecycle("✅ Applied: org.jetbrains.kotlin.plugin.serialization")

            // 버전 카탈로그 접근
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            // 네트워크 의존성 추가
            logger.lifecycle("📦 Adding Network dependencies...")

            dependencies {
                add("implementation", platform(libs.findLibrary("okhttp-bom").get()))

                // 레트로핏 번들
                val retrofit = libs.findBundle("retrofit")
                if (retrofit.isPresent) {
                    add("implementation", retrofit.get())
                    logger.lifecycle("🧩 Added retrofit bundle")
                } else {
                    // 번들이 없을 경우 개별 의존성 추가
                    add("implementation", libs.findLibrary("retrofit-core").get())
                    add("implementation", libs.findLibrary("retrofit-converter-kotlinx").get())
                    add("implementation", libs.findLibrary("okhttp").get())
                    add("implementation", libs.findLibrary("okhttp-logging").get())
                    logger.lifecycle("🧩 Added individual retrofit dependencies")
                }

                // 직렬화 및 비동기 처리
                add("implementation", libs.findLibrary("kotlinx-serialization-json").get())
                add("implementation", libs.findLibrary("kotlinx-coroutines").get())
            }

            logger.lifecycle("✅ ===============================================")
            logger.lifecycle("✅ NetworkConventionPlugin successfully applied")
            logger.lifecycle("✅ ===============================================")
        }
    }
}