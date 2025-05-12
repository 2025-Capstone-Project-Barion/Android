package com.example.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * Compose 관련 설정을 구성하는 확장 함수
 * 이 함수는 애플리케이션 및 라이브러리 모듈 모두에서 재사용 가능
 * @param commonExtension 안드로이드 빌드 설정을 포함하는 확장 객체
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    // 올바른 방식으로 VersionCatalogsExtension을 가져옴
    val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

    commonExtension.apply {
        buildFeatures {
            // Compose 사용을 활성화
            compose = true
        }

        composeOptions {
            // 버전 카탈로그에서 정의된 Compose 컴파일러 버전을 사용
            // toml 파일에 정의된 "compose-compiler" 버전을 참조
            kotlinCompilerExtensionVersion = libs.findVersion("compose-compiler").get().requiredVersion
        }
    }

    // 프로젝트에 Compose 관련 의존성 추가
    dependencies {
        // Compose BOM(Bill of Materials)을 가져와 일관된 버전의 Compose 라이브러리 사용
        // toml 파일에 정의된 "androidx-compose-bom" 참조
        val composeBom = libs.findLibrary("androidx-compose-bom").get()
        // BOM을 의존성으로 추가하여 다른 Compose 라이브러리의 버전을 일관되게 관리
        add("implementation", platform(composeBom))

        // 각 Compose 라이브러리를 의존성으로 추가
        // toml 파일에 정의된 라이브러리 ID를 사용하여 참조
        add("implementation", libs.findLibrary("androidx-ui").get())
        add("implementation", libs.findLibrary("androidx-ui-graphics").get())
        add("implementation", libs.findLibrary("androidx-ui-tooling-preview").get())
        add("implementation", libs.findLibrary("androidx-material3").get())

        // 디버그 빌드에만 필요한 의존성 추가
        add("debugImplementation", libs.findLibrary("androidx-ui-tooling").get())
        add("debugImplementation", libs.findLibrary("androidx-ui-test-manifest").get())
    }
}