package com.example.sales.screen

// feature/sales/src/main/java/com/barrion/feature/sales/screen/SalesScreen.kt


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.barrion.pos.feature.sales.component.SalesSummaryCard
import com.example.domain.model.ChartData
import com.example.domain.model.SalesSummary
import com.example.sales.component.YearSelector
import com.example.sales.type.SalesEffect
import com.example.sales.type.SalesIntent
import com.example.sales.type.SalesState
import com.example.sales.viewmodel.SalesViewModel
import com.example.ui.theme.BarrionTheme
import com.example.ui.theme.barrionColors
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.barrion.pos.feature.sales.component.SalesBarChart


/**
 * 매출 관리 화면
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesScreen(
    viewModel: SalesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Effect 처리
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SalesEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is SalesEffect.ShowSuccess -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "매출 관리",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.handleIntent(SalesIntent.RefreshData) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "새로고침"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.barrionColors.white,
                    titleContentColor = MaterialTheme.barrionColors.grayBlack,
                    actionIconContentColor = MaterialTheme.barrionColors.grayBlack
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.barrionColors.white
    ) { paddingValues ->
        SalesContent(
            state = state,
            onIntent = viewModel::handleIntent,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

/**
 * 매출 화면 컨텐츠
 */
@Composable
private fun SalesContent(
    state: SalesState,
    onIntent: (SalesIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading -> {
            LoadingContent(modifier = modifier)
        }
        state.isEmpty -> {
            EmptyContent(
                onRefresh = { onIntent(SalesIntent.RefreshData) },
                modifier = modifier
            )
        }
        state.error != null -> {
            ErrorContent(
                error = state.error,
                onRefresh = { onIntent(SalesIntent.RefreshData) },
                modifier = modifier
            )
        }
        else -> {
            SalesDataContent(
                state = state,
                onIntent = onIntent,
                modifier = modifier
            )
        }
    }
}

/**
 * 로딩 상태 컨텐츠
 */
@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.barrionColors.primaryBlue
            )
            Text(
                text = "매출 데이터 로딩 중...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.barrionColors.grayMedium
            )
        }
    }
}

/**
 * 빈 상태 컨텐츠
 */
@Composable
private fun EmptyContent(
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "매출 데이터가 없습니다",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.barrionColors.grayMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = "새로고침을 시도해보세요",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.barrionColors.grayMedium,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = onRefresh,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("새로고침")
            }
        }
    }
}

/**
 * 에러 상태 컨텐츠
 */
@Composable
private fun ErrorContent(
    error: String,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "데이터를 불러올 수 없습니다",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.barrionColors.error,
                textAlign = TextAlign.Center
            )
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.barrionColors.grayMedium,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = onRefresh,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("다시 시도")
            }
        }
    }
}

/**
 * 매출 데이터 컨텐츠
 */
@Composable
private fun SalesDataContent(
    state: SalesState,
    onIntent: (SalesIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 16.dp), // 추가 바텀 패딩
        verticalArrangement = Arrangement.spacedBy(16.dp) // 간격 조정
    ) {
        // 연도 선택기
        YearSelector(
            selectedYear = state.selectedYear,
            onYearChange = { year -> onIntent(SalesIntent.SelectYear(year)) },
            modifier = Modifier.fillMaxWidth()
        )

        // 매출 요약 카드
        state.salesSummary?.let { summary ->
            SalesSummaryCard(
                summary = summary,
                selectedYear = state.selectedYear
            )
        }

        // 월별 매출 차트
        if (state.chartData.isNotEmpty()) {
            SalesBarChart(
                chartData = state.chartData,
                onMonthClick = { month -> onIntent(SalesIntent.SelectMonth(month)) }
            )
        }
    }
}


@Preview(apiLevel = 33, showBackground = true)
@Composable
private fun SalesScreenPreview() {
    BarrionTheme {
        SalesDataContent(
            state = SalesState(
                isLoading = false,
                selectedYear = 2025,
                salesSummary = SalesSummary(
                    totalSales = 45_400_000,
                    averageMonthlySales = 3_783_333,
                    highestSalesMonth = 12,
                    lowestSalesMonth = 1,
                    highestSalesAmount = 5_200_000,
                    lowestSalesAmount = 2_100_000
                ),
                chartData = listOf(
                    ChartData(1, 2_500_000, false),
                    ChartData(2, 3_200_000, false),
                    ChartData(3, 4_100_000, true),
                    ChartData(4, 3_800_000, false),
                    ChartData(5, 4_500_000, false),
                    ChartData(6, 3_900_000, false),
                    ChartData(7, 5_200_000, false),
                    ChartData(8, 4_800_000, false),
                    ChartData(9, 4_300_000, false),
                    ChartData(10, 3_700_000, false),
                    ChartData(11, 4_000_000, false),
                    ChartData(12, 5_500_000, false)
                )
            ),
            onIntent = { }
        )
    }
}

