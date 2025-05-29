package com.example.domain.model

// domain/model/Order.kt
data class Order(
    val orderId: Int,
    val storeId: Int,
    val orderTime: String, // "2025.05.09 10:25" 형식
    val totalAmount: Int,
    val status: OrderStatus
)