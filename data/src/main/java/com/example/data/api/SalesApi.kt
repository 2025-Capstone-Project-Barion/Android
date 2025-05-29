package com.example.data.api

// data/remote/api/SalesApi.kt

import com.example.data.dto.SalesDataDto
import com.example.data.dto.TotalSalesDto
import retrofit2.http.GET

/**
 * 매출 관련 API 인터페이스
 */
interface SalesApi {

    /**
     * 총 매출 조회
     */
    @GET("sales/total")
    suspend fun getTotalSales(): TotalSalesDto

    /**
     * 연도별 매출 조회
     */
    @GET("sales/yearly")
    suspend fun getYearlySales(): List<SalesDataDto>

    /**
     * 월별 매출 조회
     */
    @GET("sales/monthly")
    suspend fun getMonthlySales(): List<SalesDataDto>
}