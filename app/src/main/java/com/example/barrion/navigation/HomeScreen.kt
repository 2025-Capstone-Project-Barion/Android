// app/src/main/java/com/barrion/navigation/HomeScreen.kt
package com.barrion.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.menu.screen.MenuScreen
import com.example.order.screen.OrderScreen
import com.example.sales.screen.SalesScreen
import com.example.staff.screen.StaffScreen
import com.example.ui.components.navigation.BarrionBottomNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var currentRoute by remember { mutableStateOf("menu") }

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
                "menu" -> MenuScreen()
                "orders" -> OrderScreen()
                "staff" -> StaffScreen()
                else -> MenuScreen() // 기본값
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}