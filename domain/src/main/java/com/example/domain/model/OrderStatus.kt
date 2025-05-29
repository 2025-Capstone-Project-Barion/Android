package com.example.domain.model

// domain/model/OrderStatus.kt

enum class OrderStatus(val displayName: String, val colorType: String) {
    RECEIVED("주문접수", "blue"),
    COMPLETED("완료", "gray"),
    CANCELLED("취소", "red");

    companion object
}
