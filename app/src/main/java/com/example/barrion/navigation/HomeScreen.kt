package com.barrion.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
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
    var currentRoute by remember { mutableStateOf("menu") }
    val menuViewModel: MenuViewModel = hiltViewModel()

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
        contentWindowInsets = WindowInsets(0, 0, 0, 0)  // 모든 inset 제거
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            when (currentRoute) {
                "sales" -> SalesScreen()
                "menu" -> MenuMviScreen(
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
                "orders" -> OrderScreen()
                "staff" -> StaffScreen()
                else -> MenuMviScreen(
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
            }
        }
    }
}