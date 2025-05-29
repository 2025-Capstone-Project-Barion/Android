package com.example.data.mapper

import com.example.data.dto.SalesDataDto
import com.example.data.dto.TotalSalesDto
import com.example.domain.model.SalesData
import com.example.domain.model.TotalSales

// data/mapper/SalesMapper.kt
/**
 * TotalSalesDto를 TotalSales 도메인 모델로 변환
 */
fun TotalSalesDto.toDomain(): TotalSales {
    return TotalSales(
        salesDate = this.salesDate,
        totalSales = this.totalSales
    )
}

/**
 * SalesDataDto를 SalesData 도메인 모델로 변환
 */
fun SalesDataDto.toDomain(): SalesData {
    return SalesData(
        salesDate = this.salesDate,
        totalSales = this.totalSales
    )
}

/**
 * SalesDataDto 리스트를 SalesData 리스트로 변환
 */
fun List<SalesDataDto>.toDomain(): List<SalesData> {
    return this.map { it.toDomain() }
}