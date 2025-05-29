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
    namespace = "com.example.feature.menu"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    // 이 부분 추가 ⬇️
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    // Lint 설정 추가 ⬇️
    lint {
        disable.add("NullSafeMutableLiveData")
        abortOnError = false
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    // 여기까지 추가 ⬆️

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    // 코어 UI 모듈 의존성
    implementation(project(":core:ui"))
    implementation(project(":domain"))
    implementation(project(":core:common"))

    // Hilt 관련 추가 (커스텀 플러그인이 제공 안할 경우)
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // 이미지 처리 및 권한 관련
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.core:core-ktx:1.12.0")

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

    // coil
    implementation("io.coil-kt:coil-compose:2.5.0")

}
