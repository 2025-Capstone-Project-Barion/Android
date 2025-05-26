package com.example.sales.type

import java.time.LocalDate

sealed class SalesIntent {
    object LoadSalesData : SalesIntent()
    data class SelectPeriod(val period: SalesPeriod) : SalesIntent()
    object RefreshData : SalesIntent()
    object ClearError : SalesIntent()
}

enum class SalesPeriod {
    TODAY, WEEK, MONTH, YEAR
}