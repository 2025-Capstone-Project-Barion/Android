package com.example.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

/**
 * 안드로이드 프로젝트의 Kotlin 관련 설정을 구성하는 확장 함수
 * 애플리케이션 및 라이브러리 모듈에서 재사용 가능
 * @param commonExtension 안드로이드 빌드 설정을 포함하는 확장 객체
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    // 올바른 방식으로 VersionCatalogsExtension을 가져옴
    val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

    commonExtension.apply {
        // 컴파일 SDK 버전 설정
        compileSdk = Const.COMPILE_SDK

        defaultConfig {
            // 최소 SDK 버전 설정
            minSdk = Const.MIN_SDK
            // 테스트 인스트루먼테이션 러너 설정
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            // 벡터 드로어블 지원 라이브러리 사용 설정
            vectorDrawables {
                useSupportLibrary = true
            }
        }

        // 빌드 타입 설정 (release 빌드에 대한 설정)
        buildTypes {
            getByName("release") {
                // 코드 난독화 비활성화 (필요에 따라 활성화할 수 있음)
                isMinifyEnabled = false
                // ProGuard 규칙 파일 설정
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }

        // Java 컴파일 옵션 설정
        compileOptions {
            // 소스 및 타겟 호환성을 Java 17로 설정
            sourceCompatibility = Const.JAVA_VERSION
            targetCompatibility = Const.JAVA_VERSION
        }

        // Kotlin 컴파일 옵션 설정 (아래에서 정의된 확장 함수 사용)
        kotlinOptions {
            // JVM 타겟을 Java 17로 설정
            jvmTarget = Const.JAVA_VERSION.toString()
        }

        // 패키징 옵션 설정
        packaging {
            resources {
                // META-INF의 특정 파일들을 패키징에서 제외
                // 일반적으로 여러 라이브러리에서 중복되는 라이센스 파일 등을 제외하기 위함
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }
}

/**
 * CommonExtension에 대한 확장 함수로, kotlinOptions 설정에 접근할 수 있게 함
 * CommonExtension은 직접적으로 kotlinOptions를 제공하지 않기 때문에 필요
 * @param block KotlinJvmOptions를 구성하는 람다 함수
 */
internal fun CommonExtension<*, *, *, *, *, *>.kotlinOptions(
    block: KotlinJvmOptions.() -> Unit
) {
    // ExtensionAware 인터페이스를 통해 'kotlinOptions' 확장에 접근하고 구성
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}