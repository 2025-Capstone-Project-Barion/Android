package com.example.sales.screen

// feature/sales/src/main/java/com/barrion/feature/sales/screen/SalesScreen.kt


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sales.type.SalesEffect
import com.example.sales.viewmodel.SalesViewModel


@Composable
fun SalesScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "📊",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "매출 관리",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "일별, 월별 매출 통계 및 분석",
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
private fun SalesScreenPreview() {
    MaterialTheme {
        SalesScreen()
    }
}