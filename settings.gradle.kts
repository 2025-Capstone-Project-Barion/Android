// Gradle 플러그인 관리 설정 (빌드 초기 단계에서 처리됨)
pluginManagement {
    // build-logic 디렉토리를 복합 빌드로 포함
    // 이를 통해 build-logic에서 정의한 Convention 플러그인을 프로젝트에서 사용 가능
    includeBuild("build-logic")

    // 플러그인을 찾을 저장소 설정
    repositories {
        // Google 저장소 - 특정 그룹만 포함하도록 제한
        google {
            content {
                includeGroupByRegex("com\\.android.*") // Android 플러그인
                includeGroupByRegex("com\\.google.*")  // Google 플러그인
                includeGroupByRegex("androidx.*")      // AndroidX 플러그인
            }
        }
        mavenCentral() // Maven Central 저장소
        gradlePluginPortal() // Gradle 플러그인 포털
    }
}

// 의존성 해결 방법 관리 설정
dependencyResolutionManagement {
    // 프로젝트별 저장소 설정을 금지하고 이 설정만 사용하도록 강제
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    // 의존성을 찾을 저장소 설정
    repositories {
        google()      // Android/Google 라이브러리용
        mavenCentral() // 대부분의 오픈소스 라이브러리용
    }
}

// 루트 프로젝트 이름 설정
rootProject.name = "Barrion"

// 프로젝트에 포함할 모듈 정의
include(":app")               // 앱 모듈 (애플리케이션 진입점)
include(":domain")            // 도메인 모듈 (비즈니스 로직, 엔티티)
include(":data")              // 데이터 모듈 (저장소, API 통신)
include(":presentation")      // 프레젠테이션 모듈 (UI 공통 로직)

// 코어 모듈 (여러 모듈에서 공유되는 기능)
include(":core:ui")           // UI 공통 요소 (컴포넌트, 테마)
include(":core:common")       // 공통 유틸리티, 확장 함수 등

// 기능별 모듈
include(":feature:auth")      // 인증 기능
include(":feature:onboarding") // 온보딩 기능
include(":feature:sales")     // 매출 관리 기능
include(":feature:menu")      // 메뉴 관리 기능
include(":feature:order")     // 주문 관리 기능
include(":feature:staff")     // 직원 관리 기능

// build-logic:convention은 복합 빌드로 포함되었으므로 include에서 제외
//include(":build-logic:convention")