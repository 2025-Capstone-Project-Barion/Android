package com.example.data.mapper

import com.example.data.dto.OrderDto
import com.example.domain.model.Order
import com.example.domain.model.OrderStatus

// DTO -> Domain 변환
fun OrderDto.toDomain(): Order {
    return Order(
        orderId = orderId,
        storeId = storeId,
        orderTime = orderDate, // orderDate -> orderTime으로 매핑
        totalAmount = totalAmount,
        status = OrderStatus.fromDisplayName(orderStatus) // orderStatus -> status로 매핑
    )
}

// DTO List -> Domain List
fun List<OrderDto>.toDomainList(): List<Order> {
    return this.map { it.toDomain() }
}

// OrderStatus enum 확장 - API 응답 문자열과 매핑
fun OrderStatus.Companion.fromDisplayName(displayName: String): OrderStatus {
    return when (displayName) {
        "string" -> OrderStatus.RECEIVED // API에서 "string"으로 오는 것 같음
        "주문접수" -> OrderStatus.RECEIVED
        "완료" -> OrderStatus.COMPLETED
        "취소" -> OrderStatus.CANCELLED
        else -> OrderStatus.RECEIVED // 기본값
    }
}