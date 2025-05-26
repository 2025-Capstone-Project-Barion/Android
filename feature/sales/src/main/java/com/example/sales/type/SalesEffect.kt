package com.example.sales.type

sealed class SalesEffect {
    data class ShowToast(val message: String) : SalesEffect()
    data class ExportSalesReport(val data: List<SalesData>) : SalesEffect()
}