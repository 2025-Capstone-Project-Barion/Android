1월 둘째주

01/07
- [ ] 15시 -  XML selector 배우기
- [ ] 기획 정리 및 PPT로 변환
- [ ] 안드로이드 3강까지 듣고 정리하기
- [ ] 알고리즘 공부
- [ ] JAVA -> 객체지향 공부
- [ ] 복싱
- [ ] 블로그 작성 후, 파일정리(작업물들)


01/09
- [ ] IOS ch 1
- [ ] 창업동아리 공지 보고 어케할지 생각하기.
- [ ] 일자리 플러스 센터 정보 얻어가기
- [ ] 1~3월까지 캡스톤 팀원 일정들 체크



5049*

할수있는거 할수없는거 명확하게


금,토



Recycle view , item이 필요하다
Item XML - > adapter 연결

1. 데이터랑 item 1ㄷ1 연결
2. List 랑 기능이랑 연결 -> 어댑터


# API 연결 예제 코드

1. Recycler View

UMC init 설정



시험 11시

공학관 411호

다익스트라, 플로이드 신장트리 이후



https://apis.data.go.kr/
B551011/PhotoGalleryService1/galleryList1?numOfRows=10&pageNo=1&MobileOS=AND&MobileApp=app&arrange=A&_type=_json&serviceKey=KVLNantZhhbecolXsyBcwYgdnmPDo0poEXjIFRJLMG4adbgyavxDN9aKnwgKfeRsvG46veAKSktHS1e8mI%2FyKQ%3D%3D


https://apis.data.go.kr/
galleryList1?pageNo=10&numOfRows=1&MobileOS=AND&MobileApp=app&arrange=A&_type=_json&serviceKey=KVLNantZhhbecolXsyBcwYgdnmPDo0poEXjIFRJLMG4adbgyavxDN9aKnwgKfeRsvG46veAKSktHS1e8mI%2FyKQ%3D%3D}

하루 일정 트래킹 앱
코틀린 멀티플랫폼(and ios)
좀 더 나은 기능들 간펀 추적및 커스텀

배포 및 출시 목표
디자인 - figma
서버 - firebase(or Ktor)
프론트 - 코틀린 멀티플랫폼(and ios)
(compose)

## Barrion
<p align="center">
  <img src="" alt="엘더케어"/>
</p> 

## 목차
- [개요](#개요)
- [내용](#내용)
- [화면](#화면)
- [개발 환경](#개발환경)
- [인프라 구조](#인프라구조)
- [기술 스택](#기술스택)

## 개요
- 프로젝트 이름: 배리어프리 스마트 키오스크 솔루션: 베리온(Barion)
- 프로젝트 지속기간: 2024.02.27 ~ 05.30
- 개발 엔진 및 언어: Android Studio, Kotlin

## 내용

## 개발 환경
<p align="center">

</p>

## 인프라 구조
<p align="center">

</p>

## 화면
<p align="center">

</p>

## 기술스택

# 기술 스택 (Tech Stack)

본 자영업자 키오스크 관리 애플리케이션은 다음과 같은 최신 기술 스택을 활용하여 개발되었습니다.

## 아키텍처 & 디자인 패턴

| 기술 | 설명 |
|------|------|
| **Clean Architecture** | 비즈니스 로직과 UI를 명확히 분리하여 코드의 유지보수성과 테스트 용이성을 높입니다. 데이터, 도메인, 프레젠테이션 계층으로 구분하여 각 계층의 책임을 명확히 합니다. |
| **MVI with Orbit** | Model-View-Intent 패턴을 Orbit 라이브러리로 구현하여 단방향 데이터 흐름을 통해 UI 상태를 예측 가능하게 관리합니다. 복잡한 상태 변화를 효율적으로 처리하고 디버깅을 용이하게 합니다. |
| **멀티모듈 구조** | 앱을 기능별 모듈로 분리하여 개발 효율성과 빌드 속도를 향상시킵니다. 각 모듈은 독립적으로 개발 및 테스트가 가능하며, 코드 재사용성과 유지보수성이 향상됩니다. |

## UI/UX

| 기술 | 설명 |
|------|------|
| **Jetpack Compose** | 선언적 UI 툴킷을 사용하여 간결하고 직관적인 UI 개발이 가능합니다. 컴포넌트 기반 접근 방식으로 UI 요소 재사용성이 높아집니다. |
| **Glassmorphism 디자인** | 투명도와 흐림 효과를 활용한 현대적인 디자인 스타일을 적용하여 직관적이고 시각적으로 매력적인 사용자 경험을 제공합니다. |

## 핵심 기술

| 카테고리 | 기술 | 설명 |
|---------|------|------|
| **언어** | Kotlin | 간결한 문법과 널 안전성, 확장 함수 등 현대적 기능을 제공하는 안드로이드 공식 언어입니다. |
| **의존성 주입** | Hilt | Dagger 기반의 의존성 주입 라이브러리로, 컴포넌트 간 결합도를 낮추고 테스트 용이성을 높입니다. |
| **네트워크** | Retrofit, OkHttp | REST API 통신을 위한 타입 안전한 HTTP 클라이언트를 제공하며, 인터셉터와 로깅 기능으로 네트워크 요청 관리를 용이하게 합니다. |
| **비동기 처리** | Coroutines, Flow | 비동기 작업을 직관적으로 처리하고, 반응형 데이터 스트림을 통해 실시간 데이터 변화를 효율적으로 UI에 반영합니다. |
| **이미지 처리** | Coil | Kotlin Coroutines 기반 이미지 로딩 라이브러리로, 메모리 효율성과 빠른 이미지 로딩을 제공합니다. |

## 개발 프로세스 & 인프라

| 카테고리 | 기술 | 설명 |
|---------|------|------|
| **개발 전략** | Git Flow | 체계적인 브랜치 관리 전략을 통해 안정적인 릴리스 관리와 효율적인 팀 협업을 가능하게 합니다. |
| **CI/CD** | GitHub Actions | 자동화된 빌드, 테스트, 배포 파이프라인을 구축하여 코드 품질을 지속적으로 관리하고 개발 효율성을 높입니다. |

## 데이터 관리

| 기술 | 설명 |
|------|------|
| **Room Database** | 로컬 데이터 저장을 위한 SQLite 추상화 라이브러리로, 오프라인 모드에서도 안정적인 데이터 접근을 제공합니다. |
| **DataStore** | 키-값 쌍 및 타입 안전한 데이터 저장을 위한 현대적인 솔루션으로, 사용자 환경설정 저장에 활용됩니다. |

이러한 기술 스택의 조합을 통해 확장 가능하고, 유지보수가 용이하며, 사용자 경험이 뛰어난 자영업자 키오스크 관리 애플리케이션을 구현했습니다.

<br/>

