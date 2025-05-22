// app/src/main/java/com/example/barrion/navigation/NavRoutes.kt

package com.example.barrion.navigation

/**
 * 앱의 네비게이션 경로를 정의하는 sealed class
 * 모든 화면 경로를 한 곳에서 관리하여 일관성을 유지하고 오타를 방지합니다.
 */
sealed class NavRoutes(val route: String) {
    /**
     * 온보딩 화면 경로 - 앱 최초 실행 시 표시되는 화면
     * 사용자 가이드 및 초기 설정을 포함합니다.
     */
    object Onboarding : NavRoutes("onboarding")

    /**
     * 홈 화면 경로 - 앱의 메인 화면
     * 메뉴 관리 기능을 제공하는 화면입니다.
     */
    object Home : NavRoutes("home")

    /**
     * 주문 화면 경로 - 주문 관리 기능
     * 주문 목록 확인, 주문 처리 등의 기능을 제공합니다.
     */
    object Order : NavRoutes("order")

    /**
     * 매출 화면 경로 - 매출 관리 및 통계
     * 매출 현황, 통계, 리포트 등을 제공합니다.
     */
    object Sales : NavRoutes("sales")

    /**
     * 직원 관리 화면 경로 - 직원 정보 및 관리
     * 직원 목록, 근태 관리, 권한 설정 등을 제공합니다.
     */
    object Staff : NavRoutes("staff")

    // 파라미터가 있는 경로 예시:
    // /**
    //  * 주문 상세 화면 경로 - 특정 주문의 상세 정보
    //  * {orderId} 파라미터를 통해 특정 주문 정보를 로드합니다.
    //  * 사용 예시: navController.navigate("order/12345")
    //  */
    // object OrderDetail : NavRoutes("order/{orderId}")

    // /**
    //  * URL에서 경로 파라미터를 추출하는 헬퍼 함수
    //  * 사용 예시: val orderId = OrderDetail.getOrderId(savedStateHandle)
    //  */
    // fun getOrderId(savedStateHandle: SavedStateHandle): String {
    //     return checkNotNull(savedStateHandle["orderId"])
    // }
}