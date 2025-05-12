@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {

    compileOnly(libs.bundles.gradle.plugins)  // 모든 Gradle 플러그인 의존성을 한 번에 추가

//    compileOnly(libs.android.gradle.plugin)
//    compileOnly(libs.kotlin.gradle.plugin)
//    compileOnly(libs.ksp.gradle.plugin)

}
gradlePlugin { // 플러그인 추가
    plugins {
        create("androidApplication") {
            id = "barrion.android.application"
            implementationClass = "com.example.convention.AndroidApplicationConventionPlugin"
        }
        create("androidLibrary") {
            id = "barrion.android.library"
            implementationClass = "com.example.convention.AndroidLibraryConventionPlugin"
        }
        create("compose") {
            id = "barrion.compose"
            implementationClass = "com.example.convention.ComposeConventionPlugin"
        }
    }
}