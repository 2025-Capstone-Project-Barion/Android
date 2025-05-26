plugins {
    id("barrion.android.library")
    id("barrion.network") // 네트워크 통신 관련
    id("barrion.hilt") // 의존성 주입 필요한 경우


    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    // kapt 대신 ksp 사용
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // 도메인 모듈 의존성
    implementation(project(":domain"))


    // Hilt - ksp 사용
    implementation("com.google.dagger:hilt-android:2.48")
    ksp("com.google.dagger:hilt-compiler:2.48")  // kapt → ksp



    // SharedPreferences는 Android 기본 라이브러리에 포함되어 있으므로
    // 따로 의존성을 추가할 필요가 없습니다.
    // androidx.core:core-ktx는 AndroidLibraryConventionPlugin에서 이미 추가됨

    // 그러나 SharedPreferences를 더 편리하게 사용하기 위한 래퍼 라이브러리를 추가할 수 있습니다.
    // 예시: PreferenceKTX (옵션)
    implementation("androidx.preference:preference-ktx:1.2.1")

    // 또는 대안으로 DataStore를 고려할 수도 있습니다.
    // implementation(libs.androidx.datastore.preferences)
    // implementation(libs.androidx.datastore.preferences.core)
}