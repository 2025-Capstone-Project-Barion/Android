// app/src/main/java/com/example/barrion/navigation/BarrionNavHost.kt (최종 버전)
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
import com.example.onboarding.presentation.SetupStoreInfoScreen
import com.example.onboarding.presentation.SetupBusinessTypeScreen
import com.example.onboarding.presentation.SetupKioskCategoryScreen

/**
 * 앱의 메인 네비게이션 호스트
 */
@Composable
fun BarrionNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Onboarding.route
    ) {
        // 온보딩 화면
        composable(route = NavRoutes.Onboarding.route) {
            OnboardingScreen(
                onNavigateToLogin = {
                    navController.navigate(NavRoutes.Welcome.route) {
                        popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // Welcome 화면
        composable(route = NavRoutes.Welcome.route) {
            WelcomeScreen(
                onNavigateToLogin = {
                    navController.navigate(NavRoutes.Login.route)
                }
            )
        }

        // 로그인 화면 - 코드별 분기
        composable(route = NavRoutes.Login.route) {
            LoginScreen(
                onNavigateToHome = {
                    // 코드 9999: 기능 시연용 바로 홈
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Welcome.route) { inclusive = true }
                    }
                },
                onNavigateToSetup = {
                    // 코드 1234: 최초 사용자 Setup 플로우
                    navController.navigate(NavRoutes.SetupStoreInfo.route) {
                        popUpTo(NavRoutes.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        // Setup 플로우 - 1단계: 상호명 입력
        composable(route = NavRoutes.SetupStoreInfo.route) {
            SetupStoreInfoScreen(
                onNavigateNext = { storeName ->
                    // TODO: 상호명 저장
                    navController.navigate(NavRoutes.SetupBusinessType.route)
                },
                onNavigateBack = {
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(NavRoutes.SetupStoreInfo.route) { inclusive = true }
                    }
                }
            )
        }

        // Setup 플로우 - 2단계: 업종 선택
        composable(route = NavRoutes.SetupBusinessType.route) {
            SetupBusinessTypeScreen(
                onNavigateNext = { businessType ->
                    // TODO: 업종 저장
                    navController.navigate(NavRoutes.SetupKioskCategory.route)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Setup 플로우 - 3단계: 키오스크 카테고리
        composable(route = NavRoutes.SetupKioskCategory.route) {
            SetupKioskCategoryScreen(
                onNavigateNext = { categories ->
                    // TODO: 카테고리 저장 및 Setup 완료 처리
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.SetupStoreInfo.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // 홈 화면
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