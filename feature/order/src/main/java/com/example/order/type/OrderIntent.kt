package com.example.order.type


sealed class OrderIntent {
    object LoadOrders : OrderIntent()
    object RefreshOrders : OrderIntent()
    data class SelectOrder(val orderId: String) : OrderIntent()
    data class UpdateOrderStatus(val orderId: String, val status: OrderStatus) : OrderIntent()
    object ClearError : OrderIntent()
}