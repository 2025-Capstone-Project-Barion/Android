package com.example.domain.usecase.sale

// domain/usecase/GetMonthlySalesUseCase.kt


import com.example.domain.model.SalesData
import com.example.domain.repository.SalesRepository
import javax.inject.Inject

class GetMonthlySalesUseCase @Inject constructor(
    private val repository: SalesRepository
) {
    suspend operator fun invoke(): Result<List<SalesData>> {
        return repository.getMonthlySales()
    }
}