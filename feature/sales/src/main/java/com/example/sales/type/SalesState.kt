package com.example.sales.type

import com.example.domain.model.ChartData
import com.example.domain.model.SalesData
import com.example.domain.model.SalesSummary
import com.example.domain.model.TotalSales
import java.time.LocalDate

/**
 * Sales 화면의 상태 (State)
 */
data class SalesState(
    val isLoading: Boolean = false,
    val totalSales: TotalSales? = null,
    val yearlySales: List<SalesData> = emptyList(),
    val monthlySales: List<SalesData> = emptyList(),
    val salesSummary: SalesSummary? = null,
    val chartData: List<ChartData> = emptyList(),
    val selectedMonth: Int? = null,
    val selectedYear: Int = 2025,
    val error: String? = null
) {
    /**
     * 빈 상태 여부
     */
    val isEmpty: Boolean
        get() = totalSales == null && yearlySales.isEmpty() && monthlySales.isEmpty()
}