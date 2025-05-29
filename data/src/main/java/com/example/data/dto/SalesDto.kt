package com.example.data.dto

// data/remote/dto/SalesDto.kt

import kotlinx.serialization.Serializable

/**
 * 총 매출 응답 DTO
 */
@Serializable
data class TotalSalesDto(
    val salesDate: String,
    val totalSales: Long
)

/**
 * 연도별/월별 매출 응답 DTO
 * (배열 형태로 응답)
 */
@Serializable
data class SalesDataDto(
    val salesDate: String,
    val totalSales: Long
)