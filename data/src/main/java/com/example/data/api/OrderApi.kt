package com.example.data.api

import com.example.data.dto.OrderDto
import retrofit2.Response
import retrofit2.http.*

interface OrderApi {
    // 매장별 주문 조회
    @GET("/api/orders/store/{storeId}")
    suspend fun getAllOrders(@Path("storeId") storeId: Int): Response<List<OrderDto>>

    // 주문 삭제
    @DELETE("/api/orders/{orderId}")
    suspend fun deleteOrder(@Path("orderId") orderId: Int): Response<Unit>

    // 단일 주문 조회 (필요시 사용)
    @GET("/api/orders/{orderId}")
    suspend fun getOrder(@Path("orderId") orderId: Int): Response<OrderDto>

    // 주문 수정 (필요시 사용)
    @PUT("/api/orders/{orderId}")
    suspend fun updateOrder(@Path("orderId") orderId: Int): Response<OrderDto>

    // 주문 생성 (필요시 사용)
    @POST("/api/orders")
    suspend fun createOrder(@Body orderRequest: Any): Response<OrderDto>
}