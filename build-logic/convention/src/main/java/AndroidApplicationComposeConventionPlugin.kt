package com.example.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * 안드로이드 애플리케이션 모듈에 Compose 설정을 적용하는 컨벤션 플러그인
 * 이 플러그인은 이미 com.android.application 플러그인이 적용된 프로젝트에만 사용해야 함
 */
class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        // 플러그인 적용 시작 로그
        target.logger.lifecycle("🎨 AndroidApplicationComposeConventionPlugin applied to: ${target.name}")
        target.logger.lifecycle("ℹ️ Configuring Compose for Android Application module: ${target.name}")

        with(target) {
            try {
                // 직접 com.android.application 플러그인 적용 안 함
                // 대신 이미 적용된 ApplicationExtension을 가져와서 사용
                val extension = extensions.getByType<ApplicationExtension>()
                configureAndroidCompose(extension)
                logger.lifecycle("✅ Successfully configured Compose for application module: ${target.name}")
            } catch (e: Exception) {
                // 오류 발생 시 로그 출력
                logger.error("❌ Failed to configure Compose for application module: ${target.name}")
                logger.error("❌ Error: ${e.message}")
                throw e
            }
        }
    }
}