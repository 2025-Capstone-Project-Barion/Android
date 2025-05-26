package com.example.order.type

sealed class OrderEffect {
    data class ShowToast(val message: String) : OrderEffect()
    data class NavigateToOrderDetail(val orderId: String) : OrderEffect()
}