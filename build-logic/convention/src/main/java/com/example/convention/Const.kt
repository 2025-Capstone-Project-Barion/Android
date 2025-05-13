package com.example.convention

import org.gradle.api.JavaVersion

/**
 * 프로젝트 전반에서 사용할 공통 상수를 정의하는 객체
 * 컴파일 SDK, 최소 SDK, 타겟 SDK 및 Java 버전과 같은 설정을 중앙에서 관리
 */

object Const {
    const val COMPILE_SDK = 35      // 컴파일 SDK 버전
    const val MIN_SDK = 21          // 최소 지원 SDK 버전
    const val TARGET_SDK = 35       // 타겟 SDK 버전
    val JAVA_VERSION = JavaVersion.VERSION_17  // 사용할 Java 버전
    const val NAMESPACE_PREFIX = "com.example.barrion"  // 패키지명 프리픽스
}