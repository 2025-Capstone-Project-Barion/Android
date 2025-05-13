package com.example.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * 이미지 로딩 라이브러리를 위한 컨벤션 플러그인
 */
class ImageLoadingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        // 로그 출력
        target.logger.lifecycle("🖼️ ===============================================")
        target.logger.lifecycle("🖼️ ImageLoadingConventionPlugin applied to: ${target.name}")
        target.logger.lifecycle("🖼️ ===============================================")

        with(target) {
            // 버전 카탈로그 접근
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            // 이미지 로딩 의존성 추가
            logger.lifecycle("📦 Adding Image Loading dependencies...")

            dependencies {
                // Glide
                add("implementation", libs.findLibrary("glide").get())
                add("ksp", libs.findLibrary("glide-compiler").get())

                // Coil (선택적)
                add("implementation", libs.findLibrary("coil").get())
            }

            logger.lifecycle("✅ ===============================================")
            logger.lifecycle("✅ ImageLoadingConventionPlugin successfully applied")
            logger.lifecycle("✅ ===============================================")
        }
    }
}