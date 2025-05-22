// app/src/main/java/com/example/barrion/navigation/BarrionNavHost.kt

package com.example.barrion.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.onboarding.presentation.OnboardingScreen

/**
 * 앱의 메인 네비게이션 호스트
 * 모든 화면 간의 이동을 관리합니다.
 *
 * @param navController 화면 전환을 위한 네비게이션 컨트롤러
 */
@Composable
fun BarrionNavHost(navController: NavHostController) {
    // 앱의 모든 화면 간 네비게이션을 설정
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Onboarding.route  // 시작 화면을 온보딩으로 설정
    ) {
        // 온보딩 화면 라우트
        composable(route = NavRoutes.Onboarding.route) {
            OnboardingScreen(
                onNavigateToHome = {
                    // 홈 화면으로 이동하면서 온보딩 화면은 백스택에서 제거
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // 홈 화면 라우트 (메뉴 관리 화면)
        composable(route = NavRoutes.Home.route) {
            // 현재 구현 예정인 화면 - 구현 후 주석 해제
            // MenuScreen(
            //     onNavigateToOrder = { navController.navigate(NavRoutes.Order.route) },
            //     onNavigateToSales = { navController.navigate(NavRoutes.Sales.route) },
            //     onNavigateToStaff = { navController.navigate(NavRoutes.Staff.route) }
            // )

            // 임시로 개발 중 메시지 표시
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "앱 개발 중...",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        // 주문 관리 화면 라우트
        composable(route = NavRoutes.Order.route) {
            // 현재 구현 예정인 화면 - 구현 후 주석 해제
            // OrderScreen(
            //     onNavigateBack = { navController.popBackStack() }
            // )
        }

        // 매출 화면 라우트
        composable(route = NavRoutes.Sales.route) {
            // 현재 구현 예정인 화면 - 구현 후 주석 해제
            // SalesScreen(
            //     onNavigateBack = { navController.popBackStack() }
            // )
        }

        // 직원 관리 화면 라우트
        composable(route = NavRoutes.Staff.route) {
            // 현재 구현 예정인 화면 - 구현 후 주석 해제
            // StaffScreen(
            //     onNavigateBack = { navController.popBackStack() }
            // )
        }

        // 추가 화면들..
    }
}