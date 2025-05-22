plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")

    // Compose 컴파일러 플러그인
    id("org.jetbrains.kotlin.plugin.compose")

    // 필요한 Hilt와 이미지 로딩 플러그인
    id("barrion.hilt")
    id("barrion.imageloading")
}

android {
    namespace = "com.example.onboarding"

    // 컴파일 SDK 버전 추가 (앱 모듈과 같은 버전 사용)
    compileSdk = 34  // 이 부분이 누락되었습니다

    defaultConfig {
        minSdk = 21  // 앱 모듈과 일치

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // 코어 UI 모듈 의존성
    implementation(project(":core:ui"))

    // Compose 기본 의존성
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Material 아이콘 확장 의존성
    implementation("androidx.compose.material:material-icons-extended:1.5.4")

    // 다른 필요한 의존성
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    // 테스트 의존성
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}