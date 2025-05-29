package com.example.ui.components.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.Spacing
import com.example.ui.theme.barrionColors

// 네비게이션 아이템 데이터 클래스
data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

// 바텀 네비게이션 아이템들 정의 - 아이콘 수정
object BottomNavItems {
    val items = listOf(
        BottomNavItem("sales", Icons.Outlined.Assessment, "매출"),
        BottomNavItem("menu", Icons.Outlined.RestaurantMenu, "메뉴"),  // 메뉴판 아이콘
        BottomNavItem("orders", Icons.Outlined.ShoppingCart, "주문"),
        BottomNavItem("staff", Icons.Outlined.Person, "직원")
    )
}

@Composable
fun BarrionBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),  // 더 작게
        containerColor = MaterialTheme.barrionColors.white,
        tonalElevation = 0.dp,  // 그림자 완전 제거
        windowInsets = WindowInsets(0, 0, 0, 0)  // 모든 여백 제거
    ) {
        BottomNavItems.items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(20.dp)  // 아이콘 크기 조정
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    // 선택된 상태
                    selectedIconColor = MaterialTheme.barrionColors.white,
                    selectedTextColor = MaterialTheme.barrionColors.white,
                    indicatorColor = MaterialTheme.barrionColors.primaryBlue,

                    // 선택되지 않은 상태
                    unselectedIconColor = MaterialTheme.barrionColors.grayMedium,
                    unselectedTextColor = MaterialTheme.barrionColors.grayMedium
                )
            )
        }
    }
}

@Preview
@Composable
private fun BarrionBottomNavigationPreview() {
    MaterialTheme {
        BarrionBottomNavigation(
            currentRoute = "menu",
            onNavigate = {}
        )
    }
}