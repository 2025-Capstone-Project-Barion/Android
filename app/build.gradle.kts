import java.util.Properties

plugins {
    // 커스텀 플러그인 적용
    id("barrion.android.application")
    id("barrion.android.application.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("barrion.hilt")
    id("barrion.network")
    id("barrion.imageloading")
}

val properties =
    Properties().apply {
        load(project.rootProject.file("local.properties").inputStream())
    }

android {
    namespace = "com.example.barrion"

    defaultConfig {
        applicationId = "com.example.barrion"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // 리소스 최적화 설정
        resourceConfigurations.addAll(listOf("en", "ko"))
    }

    // 동적 기능 모듈 관련 설정 추가
    // 온보딩 모듈이 동적 기능 모듈로 구성되어 있다면 사용
    // dynamicFeatures += setOf(":feature:onboarding")

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
}

dependencies {
    // 기존 의존성 유지
    implementation(project(":core:ui"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":core:common"))
    implementation(project(":presentation"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:menu"))
    implementation(project(":feature:sales"))
    implementation(project(":feature:order"))
    implementation(project(":feature:staff"))

    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    implementation(libs.balloon)
    implementation(libs.core.splashscreen)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

//import java.util.Properties
//
//plugins {
//    // 기존 플러그인 유지
//    alias(libs.plugins.android.application)
//    alias(libs.plugins.kotlin.android)
//    alias(libs.plugins.kotlin.compose)
//    alias(libs.plugins.kotlin.serialization)
//    alias(libs.plugins.hilt.android)
//    alias(libs.plugins.ksp)
//    alias(libs.plugins.navigation.safeargs.kotlin)
//    id("org.jetbrains.kotlin.kapt")
//
//    // 커스텀 플러그인 적용
//    id("barrion.android.application")
//}
//
//val properties =
//    Properties().apply {
//        load(project.rootProject.file("local.properties").inputStream())
//    }
//
//android {
//    namespace = "com.example.barrion"
//    compileSdk = 35
//
//    defaultConfig {
//        applicationId = "com.example.barrion"
//        minSdk = 21
//        targetSdk = 34
//        versionCode = 1
//        versionName = "1.0"
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//    kotlinOptions {
//        jvmTarget = "11"
//    }
//    buildFeatures {
//        viewBinding = true
//        dataBinding = true
//        buildConfig = true
//    }
//    buildFeatures {
//        compose = true
//    }
//}
//
//dependencies {
//    // Compose 의존성
//    implementation(libs.androidx.activity.compose)
//    implementation(platform(libs.androidx.compose.bom))
//    implementation(libs.androidx.ui)
//    implementation(libs.androidx.ui.graphics)
//    implementation(libs.androidx.ui.tooling.preview)
//    implementation(libs.androidx.material3)
//    debugImplementation(libs.androidx.ui.tooling)
//    debugImplementation(libs.androidx.ui.test.manifest)
//    androidTestImplementation(platform(libs.androidx.compose.bom))
//    androidTestImplementation(libs.androidx.ui.test.junit4)
//
//    // 기본 안드로이드 및 UI 의존성
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.lifecycle.runtime.ktx)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//    implementation(libs.androidx.activity)
//    implementation(libs.androidx.constraintlayout)
//
//    // 앱 특화 UI 의존성
//    implementation(libs.balloon)
//    implementation(libs.core.splashscreen)
//
//    // 데이터 스토리지
//    implementation(libs.androidx.datastore.preferences)
//    implementation(libs.androidx.datastore.preferences.core)
//
//    // Hilt
//    implementation(libs.hilt.android)
//    ksp(libs.hilt.compiler)
//    ksp(libs.hilt.android.compiler)
//
//    // 이미지 로딩 (Glide와 Coil)
//    implementation(libs.glide)
//    ksp(libs.glide.compiler)
//    implementation(libs.coil)  // 필요하지 않다면 제거 가능
//
//    // 유틸리티
//    implementation(libs.timber)
//    implementation(libs.androidx.viewpager2)
//
//    // 네비게이션
//    implementation(libs.bundles.navigation)
//
//    // 비동기 및 직렬화
//    implementation(libs.kotlinx.serialization.json)
//    implementation(libs.kotlinx.coroutines)
//
//    // 네트워킹
//    implementation(platform(libs.okhttp.bom))
//    implementation(libs.bundles.retrofit)
//
//    // 테스트 의존성
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//}

/* 플러그인 적용 전, 단일모듈 빌드 설정 */

//import java.util.Properties
//
//plugins {
//    alias(libs.plugins.android.application)
//    alias(libs.plugins.kotlin.android)
//    alias(libs.plugins.kotlin.compose)
//    alias(libs.plugins.kotlin.serialization)
//    alias(libs.plugins.hilt.android)
//    alias(libs.plugins.ksp)
//    alias(libs.plugins.navigation.safeargs.kotlin)
//    id("org.jetbrains.kotlin.kapt")
//}
//
//val properties =
//    Properties().apply {
//        load(project.rootProject.file("local.properties").inputStream())
//    }
//
//android {
//    namespace = "com.example.barrion"
//    compileSdk = 35
//
//    defaultConfig {
//        applicationId = "com.example.barrion"
//        minSdk = 21
//        targetSdk = 34
//        versionCode = 1
//        versionName = "1.0"
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//    kotlinOptions {
//        jvmTarget = "11"
//    }
//    buildFeatures {
//        viewBinding = true
//        dataBinding = true
//        buildConfig = true
//    }
//    buildFeatures {
//        compose = true
//    }
//}
//
//dependencies {
//
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.lifecycle.runtime.ktx)
//    implementation(libs.androidx.activity.compose)
//    implementation(platform(libs.androidx.compose.bom))
//    implementation(libs.androidx.ui)
//    implementation(libs.androidx.ui.graphics)
//    implementation(libs.androidx.ui.tooling.preview)
//    implementation(libs.androidx.material3)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(platform(libs.androidx.compose.bom))
//    androidTestImplementation(libs.androidx.ui.test.junit4)
//    debugImplementation(libs.androidx.ui.tooling)
//    debugImplementation(libs.androidx.ui.test.manifest)
//
//    implementation(libs.balloon)
//
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//    implementation(libs.androidx.activity)
//    implementation(libs.androidx.constraintlayout)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//
//    implementation(libs.balloon)
//
//    // DataStore
//    implementation(libs.androidx.datastore.preferences)
//    implementation(libs.androidx.datastore.preferences.core)
//
//    // Hilt
//    implementation(libs.hilt.android)
//    ksp(libs.hilt.compiler)
//    ksp(libs.hilt.android.compiler)
//
//    // Timber
//    implementation(libs.timber)
//
//    // Glide
//    implementation(libs.glide)
//    ksp(libs.glide.compiler) // annotationProcessor에서 kapt로 변경
//
//    // coil
//    implementation(libs.coil)
//
//    // ViewPager2
//    implementation(libs.androidx.viewpager2)
//
//    // navigation
//    implementation(libs.bundles.navigation)
//
//    // Kotlin Serialization
//    implementation(libs.kotlinx.serialization.json)
//
//    // Coroutines
//    implementation(libs.kotlinx.coroutines)
//
//    // Network
//    implementation(platform(libs.okhttp.bom))
//    implementation(libs.bundles.retrofit)
//
//    // splashscreen
//    implementation(libs.core.splashscreen)
//}