// feature/sales/component/SalesBarChart.kt
package com.barrion.pos.feature.sales.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.ChartData
import com.example.ui.theme.BarrionTheme
import com.example.ui.theme.barrionColors
import java.text.NumberFormat
import java.util.Locale

/**
 * 월별 매출 바 차트
 */
@Composable
fun SalesBarChart(
    chartData: List<ChartData>,
    onMonthClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val maxSales = chartData.maxOfOrNull { it.sales } ?: 1L

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.barrionColors.white
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 차트 제목
            Text(
                text = "월별 매출 현황",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.barrionColors.grayBlack
            )

            // 바 차트
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                chartData.forEach { data ->
                    BarItem(
                        month = data.month,
                        sales = data.sales,
                        maxSales = maxSales,
                        isHighlight = data.isHighlight,
                        onClick = { onMonthClick(data.month) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 월 레이블
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                (1..12).forEach { month ->
                    Text(
                        text = "${month}월",
                        fontSize = 10.sp,
                        color = MaterialTheme.barrionColors.grayMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

/**
 * 개별 바 아이템
 */
@Composable
private fun BarItem(
    month: Int,
    sales: Long,
    maxSales: Long,
    isHighlight: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val barHeight = if (maxSales > 0) {
        ((sales.toFloat() / maxSales.toFloat()) * 180f).dp
    } else {
        4.dp // 최소 높이
    }

    val barColor = if (isHighlight) {
        MaterialTheme.barrionColors.primaryBlue
    } else {
        MaterialTheme.barrionColors.blueLighter
    }

    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 매출 금액 표시 (높은 바만)
        if (sales > 0 && barHeight > 50.dp) {
            Text(
                text = formatShortCurrency(sales),
                fontSize = 8.sp,
                color = MaterialTheme.barrionColors.grayMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        // 바
        Box(
            modifier = Modifier
                .width(20.dp)
                .height(barHeight.coerceAtLeast(4.dp))
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                .background(barColor)
        )
    }
}

/**
 * 숫자를 짧은 통화 형식으로 포맷 (예: 1.2M, 500K)
 */
private fun formatShortCurrency(amount: Long): String {
    return when {
        amount >= 100_000_000 -> "${amount / 100_000_000}억"
        amount >= 10_000_000 -> "${amount / 10_000_000}천만"
        amount >= 1_000_000 -> "${amount / 1_000_000}백만"
        amount >= 10_000 -> "${amount / 10_000}만"
        else -> {
            val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
            formatter.format(amount)
        }
    }
}

@Preview(apiLevel = 33, showBackground = true)
@Composable
private fun SalesBarChartPreview() {
    BarrionTheme {
        SalesBarChart(
            chartData = listOf(
                ChartData(1, 2_500_000, false),
                ChartData(2, 3_200_000, false),
                ChartData(3, 4_100_000, true), // 하이라이트
                ChartData(4, 3_800_000, false),
                ChartData(5, 4_500_000, false),
                ChartData(6, 3_900_000, false),
                ChartData(7, 5_200_000, false),
                ChartData(8, 4_800_000, false),
                ChartData(9, 4_300_000, false),
                ChartData(10, 3_700_000, false),
                ChartData(11, 4_000_000, false),
                ChartData(12, 5_500_000, false)
            ),
            onMonthClick = { }
        )
    }
}