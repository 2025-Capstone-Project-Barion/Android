package com.example.domain.repository
// domain/repository/SalesRepository.kt

import com.example.domain.model.SalesData
import com.example.domain.model.TotalSales

/**
 * 매출 데이터 리포지토리 인터페이스
 */
interface SalesRepository {

    /**
     * 총 매출 조회
     */
    suspend fun getTotalSales(): Result<TotalSales>

    /**
     * 연도별 매출 조회
     */
    suspend fun getYearlySales(): Result<List<SalesData>>

    /**
     * 월별 매출 조회
     */
    suspend fun getMonthlySales(): Result<List<SalesData>>
}