@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // 버전 카탈로그를 사용한 의존성 추가
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)

    // 만약 위 방법이 작동하지 않는다면, 하드코딩된 의존성 사용:
    // compileOnly("com.android.tools.build:gradle:8.7.3")
    // compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0")
    // compileOnly("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.0.0-1.0.21")
}

gradlePlugin {
    plugins {
        // 기존 플러그인
        register("androidApplication") {
            id = "barrion.android.application"
            implementationClass = "com.example.convention.AndroidApplicationConventionPlugin"
        }

        // 안드로이드 라이브러리 컨벤션 플러그인 추가
        register("androidLibrary") {
            id = "barrion.android.library"
            implementationClass = "com.example.convention.AndroidLibraryConventionPlugin"
        }

        // JVM 라이브러리 컨벤션 플러그인 추가 (선택 사항)
        register("jvmLibrary") {
            id = "barrion.jvm.library"
            implementationClass = "com.example.convention.JvmLibraryConventionPlugin"
        }

        // 피처 모듈 컨벤션 플러그인 추가 (선택 사항)
        register("androidFeature") {
            id = "barrion.android.feature"
            implementationClass = "com.example.convention.FeatureConventionPlugin"
        }

        // 새로 추가된 플러그인들
        // 새로운 Compose 플러그인 등록
        register("androidApplicationCompose") {
            id = "barrion.android.application.compose"
            implementationClass = "com.example.convention.AndroidApplicationComposeConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "barrion.android.library.compose"
            implementationClass = "com.example.convention.AndroidLibraryComposeConventionPlugin"
        }

        register("hilt") {
            id = "barrion.hilt"
            implementationClass = "com.example.convention.HiltConventionPlugin"
        }

        register("network") {
            id = "barrion.network"
            implementationClass = "com.example.convention.NetworkConventionPlugin"
        }

        register("imageLoading") {
            id = "barrion.imageloading"
            implementationClass = "com.example.convention.ImageLoadingConventionPlugin"
        }
    }
}