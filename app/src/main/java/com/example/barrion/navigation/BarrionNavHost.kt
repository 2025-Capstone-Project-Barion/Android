package com.example.barrion.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.barrion.navigation.HomeScreen
import com.example.auth.screen.LoginScreen
import com.example.auth.screen.WelcomeScreen
import com.example.menu.screen.AddMenuScreen
import com.example.menu.screen.CategoryDetailScreen
import com.example.menu.screen.CategoryManagementScreen
import com.example.menu.screen.EditMenuScreen
import com.example.menu.screen.MenuMviScreen
import com.example.menu.viewmodel.MenuViewModel
import com.example.onboarding.presentation.OnboardingScreen
import com.example.onboarding.presentation.SetupStoreInfoScreen
import com.example.onboarding.presentation.SetupBusinessTypeScreen
import com.example.onboarding.presentation.SetupKioskCategoryScreen
import com.example.order.screen.OrderScreen
import com.example.sales.screen.SalesScreen
import com.example.staff.screen.StaffScreen

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

        // 홈 화면 - 바텀 네비게이션 포함
        composable(route = NavRoutes.Home.route) {
            HomeScreen(navController = navController)  // navController 전달
        }

        // 바텀 네비게이션 화면들
        composable(route = NavRoutes.Menu.route) {
            val viewModel: MenuViewModel = hiltViewModel()
            MenuMviScreen(
                viewModel = viewModel,
                onNavigateToCategoryManagement = {
                    navController.navigate(NavRoutes.CategoryManagement.route)
                },
                onNavigateToAddMenu = {
                    navController.navigate(NavRoutes.AddMenu.createRoute())
                },
                onNavigateToCategoryDetail = { categoryId, categoryName ->
                    navController.navigate(
                        NavRoutes.CategoryDetail.createRoute(categoryId, categoryName)
                    )
                },
                onNavigateToEditMenu = { menuId ->
                    navController.navigate(NavRoutes.EditMenu.createRoute(menuId))  // 추가
                }
            )
        }

        composable(route = NavRoutes.CategoryManagement.route) {
            val viewModel: MenuViewModel = hiltViewModel()
            CategoryManagementScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
                // onNavigateToAddCategory 파라미터 제거 (다이얼로그로 처리하므로 불필요)
            )
        }
        // 기존 composable들 아래에 추가

        composable(
            route = NavRoutes.CategoryDetail.route,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.LongType },
                navArgument("categoryName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getLong("categoryId") ?: 0L
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val viewModel: MenuViewModel = hiltViewModel()

            CategoryDetailScreen(
                categoryId = categoryId,
                categoryName = categoryName,
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToAddMenu = { categoryId ->
                    navController.navigate(NavRoutes.AddMenu.createRoute(categoryId))
                },
                onNavigateToEditMenu = { menuId ->
                    navController.navigate(NavRoutes.EditMenu.createRoute(menuId))  // 수정
                }
            )
        }

        // 기존 composable들 아래에 추가

        composable(
            route = NavRoutes.AddMenu.route,
            arguments = listOf(
                navArgument("categoryId") {
                    type = NavType.LongType
                    defaultValue = 0L
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getLong("categoryId")?.takeIf { it != 0L }
            val viewModel: MenuViewModel = hiltViewModel()

            AddMenuScreen(
                selectedCategoryId = categoryId,
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        // 기존 composable들 아래에 추가

        composable(
            route = NavRoutes.EditMenu.route,
            arguments = listOf(
                navArgument("menuId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val menuId = backStackEntry.arguments?.getLong("menuId") ?: 0L
            val viewModel: MenuViewModel = hiltViewModel()

            EditMenuScreen(
                menuId = menuId,
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }


        composable(route = NavRoutes.Orders.route) {
            OrderScreen()
        }

        composable(route = NavRoutes.Sales.route) {
            SalesScreen()
        }

        composable(route = NavRoutes.Staff.route) {
            StaffScreen()
        }
    }

}
