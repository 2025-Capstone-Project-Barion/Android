package com.example.domain.model

// domain/model/OrderSummary.kt
data class OrderSummary(
    val completedCount: Int,
    val totalCount: Int,
    val totalAmount: Int
)