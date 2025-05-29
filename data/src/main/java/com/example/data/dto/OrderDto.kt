package com.example.data.dto

// data/dto/OrderDto.kt
import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    val orderId: Int,
    val storeId: Int,
    val orderDate: String, // API 명세서와 일치
    val orderStatus: String, // API 명세서와 일치
    val items: List<OrderItemDto>, // 주문 항목 추가
    val totalAmount: Int
)

@Serializable
data class OrderItemDto(
    val menuId: Int,
    val menuName: String,
    val quantity: Int,
    val unitPrice: Int,
    val totalPrice: Int
)