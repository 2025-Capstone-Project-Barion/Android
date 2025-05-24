// app/src/main/java/com/example/barrion/navigation/NavRoutes.kt (수정된 부분)
package com.example.barrion.navigation

sealed class NavRoutes(val route: String) {
    /**
     * 로그인 화면 경로 - 앱 시작 시 표시되는 로그인 화면
     */
    object Login : NavRoutes("login")

    /**
     * 온보딩 화면 경로 - 앱 최초 실행 시 표시되는 화면
     */
    object Onboarding : NavRoutes("onboarding")
    object Welcome : NavRoutes("welcome") // 로그인버튼화면
    /**
     * 홈 화면 경로 - 앱의 메인 화면
     */
    object Home : NavRoutes("home")

    object Order : NavRoutes("order")
    object Sales : NavRoutes("sales")
    object Staff : NavRoutes("staff")
}