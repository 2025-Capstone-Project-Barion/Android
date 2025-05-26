package com.example.order.screen

// feature/order/src/main/java/com/barrion/feature/order/screen/OrderScreen.kt

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.order.type.OrderEffect
import com.example.order.viewmodel.OrderViewModel


@Composable
fun OrderScreen(
    viewModel: OrderViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    // Effect 처리
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is OrderEffect.ShowToast -> {
                    // Toast 처리
                }
                is OrderEffect.NavigateToOrderDetail -> {
                    // Navigation 처리
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "📋",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "주문 관리",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "실시간 주문 접수 및 처리 화면",
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
private fun OrderScreenPreview() {
    MaterialTheme {
        OrderScreen()
    }
}