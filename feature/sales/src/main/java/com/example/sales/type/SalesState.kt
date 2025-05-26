package com.example.sales.type

import java.time.LocalDate

data class SalesState(
    val salesData: List<SalesData> = emptyList(),
    val selectedPeriod: SalesPeriod = SalesPeriod.TODAY,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val totalSales: Long
        get() = salesData.sumOf { it.amount }

    val totalOrders: Int
        get() = salesData.sumOf { it.orderCount }

    val averageOrderValue: Long
        get() = if (totalOrders > 0) totalSales / totalOrders else 0
}

data class SalesData(
    val amount: Long,
    val orderCount: Int
)