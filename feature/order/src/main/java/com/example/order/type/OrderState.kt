package com.example.order.type

import java.time.LocalDateTime

data class OrderState(
    val orders: List<Order> = emptyList(),
    val selectedOrder: Order? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val pendingOrders: List<Order>
        get() = orders.filter { it.status == OrderStatus.PENDING }

    val preparingOrders: List<Order>
        get() = orders.filter { it.status == OrderStatus.PREPARING }

    val readyOrders: List<Order>
        get() = orders.filter { it.status == OrderStatus.READY }
}

data class Order(
    val id: String,
    val tableNumber: Int,
    val items: List<OrderItem>,
    val status: OrderStatus,
    val totalAmount: Int,
    val createdAt: LocalDateTime
)

data class OrderItem(
    val menuItemId: String,
    val menuItemName: String,
    val quantity: Int,
    val unitPrice: Int
)

enum class OrderStatus {
    PENDING, PREPARING, READY, COMPLETED, CANCELLED
}