package com.example.menu.screen

// feature/menu/src/main/java/com/barrion/feature/menu/screen/MenuMviScreen.kt

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.menu.type.MenuEffect
import com.example.menu.viewmodel.MenuViewModel


@Composable
fun MenuScreen() {
    // ViewModel 없이 간단한 화면으로 변경
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "🍽️",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "메뉴 관리",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "메뉴 등록, 수정, 삭제 화면",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "개발 예정",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
private fun MenuScreenPreview() {
    MaterialTheme {
        MenuScreen()
    }
}