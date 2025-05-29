package com.example.domain.usecase.order

import com.example.domain.repository.OrderRepository

// domain/usecase/DeleteOrderUseCase.kt
class DeleteOrderUseCase(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderId: Int): Result<Unit> {
        return repository.deleteOrder(orderId)
    }
}