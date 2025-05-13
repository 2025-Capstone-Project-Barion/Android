package com.example.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.JavaVersion
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure

/**
 * JVM 라이브러리 모듈을 위한 컨벤션 플러그인
 */
class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.logger.lifecycle("📚 JvmLibraryConventionPlugin applied to: ${target.name}")

        with(target) {
            // 플러그인 적용
            pluginManager.apply("java-library")
            pluginManager.apply("org.jetbrains.kotlin.jvm")

            // Java 버전 설정
            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }

            // Kotlin 설정
            val kotlinExtension = extensions.findByName("kotlin")
            if (kotlinExtension != null) {
                // Kotlin DSL을 사용한 설정
                project.afterEvaluate {
                    project.tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).configureEach {
                        kotlinOptions {
                            jvmTarget = "11"
                        }
                    }
                }
            }

            logger.lifecycle("✅ JvmLibraryConventionPlugin successfully applied")
        }
    }
}