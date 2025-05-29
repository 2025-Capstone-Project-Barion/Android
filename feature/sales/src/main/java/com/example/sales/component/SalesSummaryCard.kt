// feature/sales/component/SalesSummaryCard.kt
package com.barrion.pos.feature.sales.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.SalesSummary
import com.example.ui.theme.BarrionTheme
import com.example.ui.theme.barrionColors
import java.text.NumberFormat
import java.util.Locale

/**
 * 매출 요약 정보 카드
 */
@Composable
fun SalesSummaryCard(
    summary: SalesSummary,
    selectedYear: Int = 2025,
    modifier: Modifier = Modifier
) {
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
            // 제목
            Text(
                text = "${selectedYear}년 매출 요약",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.barrionColors.grayBlack,
                fontWeight = FontWeight.Bold
            )

            // 첫 번째 행: 총 매출, 월 평균 매출
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryItem(
                    title = "총 매출",
                    value = formatCurrency(summary.totalSales),
                    modifier = Modifier.weight(1f)
                )

                SummaryItem(
                    title = "월 평균 매출",
                    value = formatCurrency(summary.averageMonthlySales),
                    modifier = Modifier.weight(1f)
                )
            }

            // 두 번째 행: 최고 매출 월, 최저 매출 월
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryItem(
                    title = "최고 매출 월",
                    value = "${summary.highestSalesMonth}월",
                    modifier = Modifier.weight(1f)
                )

                SummaryItem(
                    title = "최저 매출 월",
                    value = "${summary.lowestSalesMonth}월",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * 요약 정보 아이템
 */
@Composable
private fun SummaryItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.barrionColors.grayMediumDark
        )

        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.barrionColors.grayBlack,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * 숫자를 통화 형식으로 포맷
 */
private fun formatCurrency(amount: Long): String {
    val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
    return "${formatter.format(amount)}원"
}


@Preview(apiLevel = 33, showBackground = true)
@Composable
private fun SalesSummaryCardPreview() {
    BarrionTheme {
        SalesSummaryCard(
            summary = SalesSummary(
                totalSales = 45_400_000,
                averageMonthlySales = 3_783_333,
                highestSalesMonth = 12,
                lowestSalesMonth = 1,
                highestSalesAmount = 5_200_000,
                lowestSalesAmount = 2_100_000
            ),
            selectedYear = 2025
        )
    }
}