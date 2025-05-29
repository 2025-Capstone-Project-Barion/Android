package com.example.sales.type



// feature/sales/type/SalesIntent.kt

/**
 * Sales 화면의 사용자 의도 (Intent)
 */
sealed class SalesIntent {
    /**
     * 매출 데이터 로딩
     */
    object LoadSalesData : SalesIntent()

    /**
     * 데이터 새로고침
     */
    object RefreshData : SalesIntent()

    /**
     * 월 선택
     */
    data class SelectMonth(val month: Int) : SalesIntent()

    /**
     * 연도 선택
     */
    data class SelectYear(val year: Int) : SalesIntent()
}