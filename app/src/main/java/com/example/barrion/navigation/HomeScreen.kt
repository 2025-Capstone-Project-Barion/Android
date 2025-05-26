package com.barrion.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.barrion.navigation.NavRoutes
import com.example.menu.screen.MenuMviScreen
import com.example.menu.viewmodel.MenuViewModel
import com.example.order.screen.OrderScreen
import com.example.sales.screen.SalesScreen
import com.example.staff.screen.StaffScreen
import com.example.ui.components.navigation.BarrionBottomNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController  // 추가
) {
    var currentRoute by remember { mutableStateOf("menu") }

    // ViewModel 생성
    val menuViewModel: MenuViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (currentRoute) {
                            "sales" -> "매출 관리"
                            "menu" -> "메뉴 관리"
                            "orders" -> "주문 관리"
                            "staff" -> "직원 관리"
                            else -> "Barrion"
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            BarrionBottomNavigation(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    currentRoute = route
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentRoute) {
                "sales" -> SalesScreen()
                "menu" -> MenuMviScreen(
                    viewModel = menuViewModel,
                    onNavigateToCategoryManagement = {
                        println("HomeScreen: 실제 네비게이션 실행")  // 디버그 로그
                        navController.navigate(NavRoutes.CategoryManagement.route)
                    },
                    onNavigateToAddMenu = { },
                    onNavigateToCategoryDetail = { _, _ -> }
                )
                "orders" -> OrderScreen()
                "staff" -> StaffScreen()
                else -> MenuMviScreen(
                    viewModel = menuViewModel,
                    onNavigateToCategoryManagement = {
                        println("HomeScreen: 실제 네비게이션 실행 (else)")
                        navController.navigate(NavRoutes.CategoryManagement.route)
                    },
                    onNavigateToAddMenu = { },
                    onNavigateToCategoryDetail = { _, _ -> }
                )
            }
        }
    }
}
