package com.example.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * 안드로이드 라이브러리 모듈에 Compose 설정을 적용하는 컨벤션 플러그인
 * 이 플러그인은 이미 com.android.library 플러그인이 적용된 프로젝트에만 사용해야 함
 */
class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        // 플러그인 적용 시작 로그
        target.logger.lifecycle("🎨 AndroidLibraryComposeConventionPlugin applied to: ${target.name}")
        target.logger.lifecycle("ℹ️ Configuring Compose for Android Library module: ${target.name}")

        with(target) {
            // Kotlin 2.0부터 필요한 Compose 컴파일러 플러그인 추가
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
            logger.lifecycle("✅ Applied: org.jetbrains.kotlin.plugin.compose")

            try {
                // 직접 com.android.library 플러그인 적용 안 함
                // 대신 이미 적용된 LibraryExtension을 가져와서 사용
                val extension = extensions.getByType<LibraryExtension>()
                configureAndroidCompose(extension)
                logger.lifecycle("✅ Successfully configured Compose for library module: ${target.name}")
            } catch (e: Exception) {
                // 오류 발생 시 로그 출력
                logger.error("❌ Failed to configure Compose for library module: ${target.name}")
                logger.error("❌ Error: ${e.message}")
                throw e
            }
        }
    }
}