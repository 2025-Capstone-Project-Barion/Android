// HomeScreen.kt - 바텀 네비게이션이 포함된 홈 화면
package com.barrion.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.barrion.navigation.NavRoutes
import com.example.menu.screen.MenuMviScreen
import com.example.menu.viewmodel.MenuViewModel
import com.example.order.screen.OrderScreen
import com.example.sales.screen.SalesScreen
import com.example.staff.screen.StaffScreen
import com.example.ui.components.navigation.BarrionBottomNavigation
import com.example.ui.theme.barrionColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController
) {
    // ✅ rememberSaveable로 상태 완전 보존
    var currentRoute by rememberSaveable { mutableStateOf(NavRoutes.Menu.route) }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val menuViewModel: MenuViewModel = hiltViewModel()

    // ✅ 백스택 변화 감지하여 적절한 탭 상태 유지
    LaunchedEffect(currentBackStackEntry) {
        val route = currentBackStackEntry?.destination?.route

        // Staff 관련 화면에서 Home으로 돌아올 때만 Staff 탭 활성화
        when {
            route == NavRoutes.Home.route -> {
                // Home으로 직접 돌아온 경우, 마지막 활성 탭 유지
                // 아무것도 하지 않음 (기존 currentRoute 유지)
            }
            route?.contains("staff") == true -> {
                currentRoute = NavRoutes.Staff.route
            }
            route?.contains("menu") == true ||
                    route?.contains("category") == true -> {
                currentRoute = NavRoutes.Menu.route
            }
            route?.contains("order") == true -> {
                currentRoute = NavRoutes.Orders.route
            }
            route?.contains("sales") == true -> {
                currentRoute = NavRoutes.Sales.route
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.barrionColors.white,
        bottomBar = {
            BarrionBottomNavigation(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    currentRoute = route
                }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            when (currentRoute) {
                NavRoutes.Sales.route -> SalesScreen()
                NavRoutes.Orders.route -> OrderScreen()

                // ✅ StaffScreen에서 onNavigateToEdit 파라미터 제거
                NavRoutes.Staff.route -> StaffScreen(
                    onNavigateToDetail = { staffId ->
                        navController.navigate(NavRoutes.StaffDetail.createRoute(staffId))
                    },
                    onNavigateToAdd = {
                        navController.navigate(NavRoutes.StaffEdit.createRoute())
                    }
                    // ✅ onNavigateToEdit 제거됨
                )

                NavRoutes.Menu.route -> MenuMviScreen(
                    viewModel = menuViewModel,
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
                        navController.navigate(NavRoutes.EditMenu.createRoute(menuId))
                    }
                )
                else -> Text("정의되지 않은 경로입니다.")
            }
        }
    }
}