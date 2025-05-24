// app/src/main/java/com/example/barrion/navigation/BarrionNavHost.kt (수정된 부분)
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
import com.example.auth.screen.LoginScreen
import com.example.auth.screen.WelcomeScreen
import com.example.onboarding.presentation.OnboardingScreen

/**
 * 앱의 메인 네비게이션 호스트
 */
@Composable
fun BarrionNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Onboarding.route  // 시작 화면을 온보딩으로 설정
    ) {
        // 온보딩 화면 라우트
        composable(route = NavRoutes.Onboarding.route) {
            OnboardingScreen(
                onNavigateToLogin = {
                    // Welcome 화면으로 이동하면서 온보딩 화면은 백스택에서 제거
                    navController.navigate(NavRoutes.Welcome.route) {
                        popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // Welcome 화면 라우트 (새로 추가)
        composable(route = NavRoutes.Welcome.route) {
            WelcomeScreen(
                onNavigateToLogin = {
                    // 로그인 화면으로 이동
                    navController.navigate(NavRoutes.Login.route)
                }
            )
        }

        // 로그인 화면 라우트
        composable(route = NavRoutes.Login.route) {
            LoginScreen(
                onNavigateToHome = {
                    // 홈 화면으로 이동하면서 이전 화면들은 백스택에서 제거
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        // 홈 화면 라우트
        composable(route = NavRoutes.Home.route) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "홈 화면 (개발 중...)",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
        // 나머지 화면들...
        composable(route = NavRoutes.Order.route) { /* 구현 예정 */ }
        composable(route = NavRoutes.Sales.route) { /* 구현 예정 */ }
        composable(route = NavRoutes.Staff.route) { /* 구현 예정 */ }
    }
}