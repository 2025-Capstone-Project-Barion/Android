// NavRoutes.kt - 네비게이션 경로 정의
package com.example.barrion.navigation

// 앱 전체의 네비게이션 경로를 정의한 sealed class
sealed class NavRoutes(val route: String) {

    // 온보딩 플로우 (최초 실행 시)
    object Onboarding : NavRoutes("onboarding")
    object Welcome : NavRoutes("welcome")
    object Login : NavRoutes("login")
    object Home : NavRoutes("home")

    // 설정 플로우 (Setup)
    object SetupStoreInfo : NavRoutes("setup_store_info")        // 상호명 입력 화면
    object SetupBusinessType : NavRoutes("setup_business_type")  // 업종 선택 화면
    object SetupKioskCategory : NavRoutes("setup_kiosk_category") // 키오스크 카테고리 선택 화면

    // 바텀 탭 화면들
    object Menu : NavRoutes("menu")            // 메뉴 관리 탭
    object Orders : NavRoutes("orders")        // 주문 관리 탭
    object Sales : NavRoutes("sales")          // 매출 탭
    object Staff : NavRoutes("staff")          // 직원 관리 탭

    // 메뉴 관리 관련 상세 화면들
    object CategoryManagement : NavRoutes("category_management") // 카테고리 전체 관리 화면

    object CategoryDetail : NavRoutes("category_detail/{categoryId}/{categoryName}") {
        fun createRoute(categoryId: Long, categoryName: String): String =
            "category_detail/$categoryId/$categoryName"  // 특정 카테고리 내 메뉴 보기
    }

    object AddMenu : NavRoutes("add_menu?categoryId={categoryId}") {
        fun createRoute(categoryId: Long? = null): String =
            categoryId?.let { "add_menu?categoryId=$it" } ?: "add_menu" // 새 메뉴 등록
    }

    object EditMenu : NavRoutes("edit_menu/{menuId}") {
        fun createRoute(menuId: Long): String = "edit_menu/$menuId"   // 기존 메뉴 수정
    }

    // 직원 관리 관련 상세 화면들
    object StaffDetail : NavRoutes("staff_detail/{staffId}") {
        fun createRoute(staffId: Long): String = "staff_detail/$staffId" // 직원 상세 보기
    }

    object StaffEdit : NavRoutes("staff_edit?id={id}") {
        fun createRoute(id: Long? = null): String =
            id?.let { "staff_edit?id=$it" } ?: "staff_edit" // 직원 등록 또는 수정
    }
}