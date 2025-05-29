package com.example.domain.repository

import com.example.domain.model.Order

interface OrderRepository {
    suspend fun getAllOrders(storeId: Int): Result<List<Order>>
    suspend fun deleteOrder(orderId: Int): Result<Unit>
    suspend fun getOrder(orderId: Int): Result<Order>
}