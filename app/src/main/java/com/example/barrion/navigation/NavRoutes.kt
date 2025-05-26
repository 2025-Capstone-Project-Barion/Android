// app/src/main/java/com/example/barrion/navigation/NavRoutes.kt (수정된 부분)
package com.example.barrion.navigation

sealed class NavRoutes(val route: String) {
    /**
     * 온보딩 화면 경로 - 앱 최초 실행 시 표시되는 화면
     */
    object Onboarding : NavRoutes("onboarding")

    /**
     * Welcome 화면 경로 - 로그인 버튼이 있는 중간 화면
     */
    object Welcome : NavRoutes("welcome")

    /**
     * 로그인 화면 경로 - 코드 입력 화면
     */
    object Login : NavRoutes("login")

    /**
     * 홈 화면 경로 - 바텀 네비게이션이 포함된 메인 화면
     */
    object Home : NavRoutes("home")

    // Setup 플로우
    object SetupStoreInfo : NavRoutes("setup_store_info")
    object SetupBusinessType : NavRoutes("setup_business_type")
    object SetupKioskCategory : NavRoutes("setup_kiosk_category")

    // 바텀 네비게이션 화면들
    object Menu : NavRoutes("menu")
    object Orders : NavRoutes("orders")
    object Sales : NavRoutes("sales")
    object Staff : NavRoutes("staff")

    // Menu
    object CategoryManagement : NavRoutes("category_management")

    object CategoryDetail : NavRoutes("category_detail/{categoryId}/{categoryName}") {
        fun createRoute(categoryId: Long, categoryName: String): String {
            return "category_detail/$categoryId/$categoryName"
        }
    }
    object AddMenu : NavRoutes("add_menu?categoryId={categoryId}") {
        fun createRoute(categoryId: Long? = null): String {
            return if (categoryId != null) {
                "add_menu?categoryId=$categoryId"
            } else {
                "add_menu"
            }
        }
    }

    object EditMenu : NavRoutes("edit_menu/{menuId}") {
        fun createRoute(menuId: Long): String {
            return "edit_menu/$menuId"
        }
    }
}