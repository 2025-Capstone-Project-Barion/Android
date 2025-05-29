package com.example.domain.model

// domain/model/Sales.kt

/**
 * 매출 데이터 도메인 모델
 */
data class SalesData(
    val salesDate: String,
    val totalSales: Long
)

/**
 * 총 매출 정보
 */
data class TotalSales(
    val salesDate: String,
    val totalSales: Long
)

/**
 * 매출 요약 정보 (UI에서 계산된 데이터)
 */
data class SalesSummary(
    val totalSales: Long,
    val averageMonthlySales: Long,
    val highestSalesMonth: Int,
    val lowestSalesMonth: Int,
    val highestSalesAmount: Long,
    val lowestSalesAmount: Long
) {
    companion object {
        fun fromMonthlySales(monthlySales: List<SalesData>): SalesSummary {
            if (monthlySales.isEmpty()) {
                return SalesSummary(0, 0, 1, 1, 0, 0)
            }

            val total = monthlySales.sumOf { it.totalSales }
            val average = total / monthlySales.size
            val maxSales = monthlySales.maxByOrNull { it.totalSales }
            val minSales = monthlySales.minByOrNull { it.totalSales }

            return SalesSummary(
                totalSales = total,
                averageMonthlySales = average,
                highestSalesMonth = extractMonth(maxSales?.salesDate ?: ""),
                lowestSalesMonth = extractMonth(minSales?.salesDate ?: ""),
                highestSalesAmount = maxSales?.totalSales ?: 0,
                lowestSalesAmount = minSales?.totalSales ?: 0
            )
        }

        private fun extractMonth(dateString: String): Int {
            return try {
                // "2025-05-01T00:00:00" 형식에서 월 추출
                dateString.substring(5, 7).toInt()
            } catch (e: Exception) {
                1
            }
        }
    }
}

/**
 * 차트용 데이터 모델
 */
data class ChartData(
    val month: Int,
    val sales: Long,
    val isHighlight: Boolean = false
) {
    companion object {
        fun fromMonthlySales(monthlySales: List<SalesData>, highlightMonth: Int? = null): List<ChartData> {
            // 1월부터 12월까지 모든 월 데이터 생성
            val monthlyMap = monthlySales.associateBy {
                extractMonth(it.salesDate)
            }

            return (1..12).map { month ->
                ChartData(
                    month = month,
                    sales = monthlyMap[month]?.totalSales ?: 0,
                    isHighlight = month == highlightMonth
                )
            }
        }

        private fun extractMonth(dateString: String): Int {
            return try {
                dateString.substring(5, 7).toInt()
            } catch (e: Exception) {
                1
            }
        }
    }
}