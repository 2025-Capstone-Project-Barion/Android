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
        register("androidApplication") {
            id = "barrion.android.application"
            implementationClass = "com.example.convention.AndroidApplicationConventionPlugin"
        }
    }
}