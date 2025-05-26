package com.example.staff.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.staff.type.StaffEffect
import com.example.staff.viewmodel.StaffViewModel

@Composable
fun StaffScreen(
    viewModel: StaffViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    // Effect 처리
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is StaffEffect.ShowToast -> {
                    // Toast 처리
                }
                is StaffEffect.NavigateToStaffDetail -> {
                    // Navigation 처리
                }
                is StaffEffect.NavigateToStaffAdd -> {
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
                text = "👤",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "직원 관리",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "직원 등록 및 권한 관리 화면",
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
private fun StaffScreenPreview() {
    MaterialTheme {
        StaffScreen()
    }
}