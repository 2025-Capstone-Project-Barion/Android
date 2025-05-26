// core/ui/src/main/java/com/barrion/core/ui/components/navigation/BottomNavigationBar.kt
package com.example.ui.components.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// 네비게이션 아이템 데이터 클래스
data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

// 바텀 네비게이션 아이템들 정의
object BottomNavItems {
    val items = listOf(
        BottomNavItem("sales", Icons.Default.Star, "매출"),
        BottomNavItem("menu", Icons.Default.Home, "메뉴"),
        BottomNavItem("orders", Icons.Default.Notifications, "주문"),
        BottomNavItem("staff", Icons.Default.Person, "직원")
    )
}

@Composable
fun BarrionBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        BottomNavItems.items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
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
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
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