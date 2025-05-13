plugins {
    id("barrion.android.feature")
    // 필요에 따라 추가 플러그인 적용
    id("barrion.hilt")
    id("barrion.imageloading")
}

android {
    namespace = "com.example.feature.auth" // 각 모듈에 맞는 네임스페이스 사용
}

dependencies {
    // 모듈 특화 의존성만 추가
}