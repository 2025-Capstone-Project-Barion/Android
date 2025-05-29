package com.example.domain.usecase.order

import com.example.domain.model.Order
import com.example.domain.repository.OrderRepository

class GetAllOrdersUseCase(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(storeId: Int): Result<List<Order>> {
        return repository.getAllOrders(storeId)
    }
}