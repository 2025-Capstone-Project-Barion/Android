package com.example.order.type

sealed class OrderIntent {
    object LoadOrders : OrderIntent()
    data class DeleteOrder(val orderId: Int) : OrderIntent()
    object RefreshData : OrderIntent()
}