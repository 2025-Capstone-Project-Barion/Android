package com.example.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * 앱 모듈용 기본 컨벤션 플러그인
 * 다른 특화 플러그인으로 분리된 기능을 제외하고 기본 앱 설정만 담당
 */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        // 플러그인이 적용될 때 눈에 띄는 로그 출력
        target.logger.lifecycle("🟢 ===============================================")
        target.logger.lifecycle("🟢 AndroidApplicationConventionPlugin applied to: ${target.name}")
        target.logger.lifecycle("🟢 ===============================================")

        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("androidx.navigation.safeargs.kotlin")

                // 각 플러그인이 적용되었는지 로그로 확인
                logger.lifecycle("✅ Applied: com.android.application")
                logger.lifecycle("✅ Applied: org.jetbrains.kotlin.android")
                logger.lifecycle("✅ Applied: androidx.navigation.safeargs.kotlin")
            }

            // 버전 카탈로그에 접근
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            logger.lifecycle("📚 Accessed version catalog: libs")

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                logger.lifecycle("⚙️ Configured Kotlin Android settings")

                defaultConfig {
                    applicationId = "com.example.barrion"
                    targetSdk = Const.TARGET_SDK
                    versionCode = 1
                    versionName = "1.0"
                }
                logger.lifecycle("📱 Set application default config")

                buildFeatures {
                    viewBinding = true
                    dataBinding = true
                    buildConfig = true
                }
                logger.lifecycle("🛠️ Configured build features")
            }

            // 의존성 추가 시 확인 로그
            logger.lifecycle("📦 Adding core dependencies...")

            // 앱 모듈 핵심 의존성 추가 (다른 플러그인으로 이동된 의존성 제외)
            dependencies {
                // 기본 안드로이드 의존성만 유지
                add("implementation", libs.findLibrary("androidx-core-ktx").get())
                add("implementation", libs.findLibrary("androidx-appcompat").get())
                add("implementation", libs.findLibrary("material").get())
                add("implementation", libs.findLibrary("androidx-activity").get())
                add("implementation", libs.findLibrary("androidx-constraintlayout").get())
                add("implementation", libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
                add("implementation", libs.findLibrary("timber").get())
                add("implementation", libs.findLibrary("androidx-viewpager2").get())

                // DataStore 관련 (나중에 별도 플러그인으로 분리 가능)
                add("implementation", libs.findLibrary("androidx-datastore-preferences").get())
                add("implementation", libs.findLibrary("androidx-datastore-preferences-core").get())
            }

            // 플러그인 적용 완료 메시지
            logger.lifecycle("✅ ===============================================")
            logger.lifecycle("✅ AndroidBaseConvention successfully applied")
            logger.lifecycle("✅ ===============================================")

            // 확인용 태스크 추가 (선택 사항)
            tasks.register("verifyApplicationPlugin") {
                doLast {
                    logger.lifecycle("🔍 Verifying AndroidApplicationConventionPlugin...")
                    logger.lifecycle("✅ AndroidApplicationConventionPlugin is correctly applied to: ${project.name}")
                }
            }
        }
    }
}




//package com.example.convention
//
//import com.android.build.api.dsl.ApplicationExtension
//import org.gradle.api.Plugin
//import org.gradle.api.Project
//import org.gradle.api.artifacts.VersionCatalogsExtension
//import org.gradle.kotlin.dsl.configure
//import org.gradle.kotlin.dsl.dependencies
//import org.gradle.kotlin.dsl.getByType
//
///**
// * 앱 모듈용 컨벤션 플러그인
// */
//class AndroidApplicationConventionPlugin : Plugin<Project> {
//    override fun apply(target: Project) {
//        // 플러그인이 적용될 때 눈에 띄는 로그 출력
//        target.logger.lifecycle("🟢 ===============================================")
//        target.logger.lifecycle("🟢 AndroidApplicationConventionPlugin applied to: ${target.name}")
//        target.logger.lifecycle("🟢 ===============================================")
//
//        with(target) {
//            with(pluginManager) {
//                apply("com.android.application")
//                apply("org.jetbrains.kotlin.android")
//                apply("org.jetbrains.kotlin.kapt")
//                apply("androidx.navigation.safeargs.kotlin")
//
//                // 각 플러그인이 적용되었는지 로그로 확인
//                logger.lifecycle("✅ Applied: com.android.application")
//                logger.lifecycle("✅ Applied: org.jetbrains.kotlin.android")
//                logger.lifecycle("✅ Applied: org.jetbrains.kotlin.kapt")
//                logger.lifecycle("✅ Applied: androidx.navigation.safeargs.kotlin")
//            }
//
//            // 버전 카탈로그에 접근
//            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
//            logger.lifecycle("📚 Accessed version catalog: libs")
//
//            extensions.configure<ApplicationExtension> {
//                configureKotlinAndroid(this)
//                logger.lifecycle("⚙️ Configured Kotlin Android settings")
//
//                defaultConfig {
//                    applicationId = "com.example.barrion"
//                    targetSdk = Const.TARGET_SDK
//                    versionCode = 1
//                    versionName = "1.0"
//                }
//                logger.lifecycle("📱 Set application default config")
//
//                buildFeatures {
//                    viewBinding = true
//                    dataBinding = true
//                    buildConfig = true
//                }
//                logger.lifecycle("🛠️ Configured build features")
//            }
//
//            // 의존성 추가 시 확인 로그
//            logger.lifecycle("📦 Adding dependencies...")
//
//            // 앱 모듈 공통 의존성 추가
//            dependencies {
//                // 기본 의존성
//                add("implementation", libs.findLibrary("androidx-core-ktx").get())
//                add("implementation", libs.findLibrary("androidx-appcompat").get())
//                add("implementation", libs.findLibrary("material").get())
//                add("implementation", libs.findLibrary("androidx-activity").get())
//                add("implementation", libs.findLibrary("androidx-constraintlayout").get())
//                add("implementation", libs.findLibrary("androidx-datastore-preferences").get())
//                add("implementation", libs.findLibrary("androidx-datastore-preferences-core").get())
//                add("implementation", libs.findLibrary("timber").get())
//                add("implementation", libs.findLibrary("androidx-viewpager2").get())
//                add("implementation", libs.findLibrary("kotlinx-serialization-json").get())
//                add("implementation", libs.findLibrary("kotlinx-coroutines").get())
//
//                // 네비게이션 번들 사용
//                add("implementation", platform(libs.findLibrary("okhttp-bom").get()))
//
//                // 번들 접근 방식 수정
//                val navigation = libs.findBundle("navigation")
//                if (navigation.isPresent) {
//                    add("implementation", navigation.get())
//                    logger.lifecycle("🧩 Added navigation bundle")
//                } else {
//                    // 번들이 없을 경우 개별 의존성 추가
//                    add("implementation", libs.findLibrary("androidx-navigation-fragment-ktx").get())
//                    add("implementation", libs.findLibrary("androidx-navigation-ui-ktx").get())
//                    logger.lifecycle("🧩 Added individual navigation dependencies")
//                }
//
//                // 레트로핏 번들
//                val retrofit = libs.findBundle("retrofit")
//                if (retrofit.isPresent) {
//                    add("implementation", retrofit.get())
//                    logger.lifecycle("🧩 Added retrofit bundle")
//                } else {
//                    // 번들이 없을 경우 개별 의존성 추가
//                    add("implementation", libs.findLibrary("retrofit-core").get())
//                    add("implementation", libs.findLibrary("retrofit-converter-kotlinx").get())
//                    add("implementation", libs.findLibrary("okhttp").get())
//                    add("implementation", libs.findLibrary("okhttp-logging").get())
//                    logger.lifecycle("🧩 Added individual retrofit dependencies")
//                }
//            }
//
//            // 플러그인 적용 완료 메시지
//            logger.lifecycle("✅ ===============================================")
//            logger.lifecycle("✅ AndroidApplicationConventionPlugin successfully applied")
//            logger.lifecycle("✅ ===============================================")
//
//            // 확인용 태스크 추가 (선택 사항)
//            tasks.register("verifyApplicationPlugin") {
//                doLast {
//                    logger.lifecycle("🔍 Verifying AndroidApplicationConventionPlugin...")
//                    logger.lifecycle("✅ AndroidApplicationConventionPlugin is correctly applied to: ${project.name}")
//                }
//            }
//        }
//    }
//}