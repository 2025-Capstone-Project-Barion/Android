package com.example.order.type

// feature/order/OrderEffect.kt
sealed class OrderEffect {
    data class ShowError(val message: String) : OrderEffect()
    data class ShowDeleteSuccess(val orderNumber: String) : OrderEffect()
    object NavigateToOrderDetail : OrderEffect()
}