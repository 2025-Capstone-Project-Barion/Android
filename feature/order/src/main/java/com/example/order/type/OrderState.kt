package com.example.order.type

import com.example.domain.model.Order
import com.example.domain.model.OrderSummary

data class OrderState(
    val orders: List<Order> = emptyList(),
    val summary: OrderSummary = OrderSummary(0, 0, 0),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    // 주문 목록이 비어있는지 확인
    val isEmpty: Boolean
        get() = !isLoading && orders.isEmpty()

    // 주문 목록에서 자동으로 요약 정보 계산
    fun calculateSummary(): OrderSummary {
        val validOrders = orders.filter { it.totalAmount > 0 } // 환불 제외
        val completedCount = validOrders.size
        val totalCount = orders.size
        val totalAmount = validOrders.sumOf { it.totalAmount }

        return OrderSummary(
            completedCount = completedCount,
            totalCount = totalCount,
            totalAmount = totalAmount
        )
    }
}