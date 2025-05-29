package com.example.domain.usecase.order

import com.example.domain.model.Order
import com.example.domain.repository.OrderRepository

class GetOrderUseCase(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderId: Int): Result<Order> {
        return repository.getOrder(orderId)
    }
}