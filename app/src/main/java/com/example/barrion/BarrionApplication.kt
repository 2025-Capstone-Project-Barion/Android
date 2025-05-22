// app/src/main/java/com/example/barrion/BarrionApplication.kt

package com.example.barrion

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * 앱의 애플리케이션 클래스
 * Hilt 의존성 주입을 위한 진입점 역할을 합니다.
 *
 * @HiltAndroidApp 어노테이션은 Hilt의 코드 생성을 트리거하고
 * 애플리케이션 수준의 의존성 컨테이너를 생성합니다.
 */
@HiltAndroidApp
class BarrionApplication : Application() {
    // 앱 시작 시 초기화가 필요한 코드를 여기에 추가할 수 있습니다.
    // 예: 로깅 설정, 크래시 리포팅 도구 초기화, 글로벌 설정 등
}