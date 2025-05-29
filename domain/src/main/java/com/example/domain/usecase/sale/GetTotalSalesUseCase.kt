package com.example.domain.usecase.sale

// domain/usecase/GetTotalSalesUseCase.kt

import com.example.domain.model.TotalSales
import com.example.domain.repository.SalesRepository
import javax.inject.Inject

class GetTotalSalesUseCase @Inject constructor(
    private val repository: SalesRepository
) {
    suspend operator fun invoke(): Result<TotalSales> {
        return repository.getTotalSales()
    }
}