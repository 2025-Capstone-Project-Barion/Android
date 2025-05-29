package com.example.data.repository

import android.util.Log
import com.example.data.api.OrderApi
import com.example.data.mapper.toDomain
import com.example.data.mapper.toDomainList
import com.example.domain.model.Order
import com.example.domain.repository.OrderRepository



class OrderRepositoryImpl(
    private val api: OrderApi
) : OrderRepository {

    override suspend fun getAllOrders(storeId: Int): Result<List<Order>> {
        return try {
            Log.d("OrderRepository", "🔍 매장 $storeId 주문 목록 조회 시작")
            val response = api.getAllOrders(storeId)

            if (response.isSuccessful) {
                val orderDtos = response.body() ?: emptyList()
                Log.d("OrderRepository", "📡 서버 응답 성공: ${orderDtos.size}개 주문")
                Log.d("OrderRepository", "📊 응답 데이터: $orderDtos")

                val orders = orderDtos.toDomainList()
                Log.d("OrderRepository", "✅ 도메인 변환 완료: $orders")
                Result.success(orders)
            } else {
                Log.e("OrderRepository", "❌ API 응답 실패: ${response.code()}")
                Result.failure(Exception("주문 목록 조회 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("OrderRepository", "❌ 네트워크 에러: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun deleteOrder(orderId: Int): Result<Unit> {
        return try {
            Log.d("OrderRepository", "🗑️ 주문 삭제 시작: ID $orderId")
            val response = api.deleteOrder(orderId)

            if (response.isSuccessful) {
                Log.d("OrderRepository", "✅ 주문 삭제 성공: ID $orderId")
                Result.success(Unit)
            } else {
                Log.e("OrderRepository", "❌ 주문 삭제 실패: ${response.code()}")
                Result.failure(Exception("주문 삭제 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("OrderRepository", "❌ 삭제 중 에러: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getOrder(orderId: Int): Result<Order> {
        return try {
            Log.d("OrderRepository", "🔍 단일 주문 조회 시작: ID $orderId")
            val response = api.getOrder(orderId)

            if (response.isSuccessful) {
                val orderDto = response.body()
                if (orderDto != null) {
                    Log.d("OrderRepository", "✅ 주문 조회 성공: $orderDto")
                    val order = orderDto.toDomain()
                    Result.success(order)
                } else {
                    Log.e("OrderRepository", "❌ 주문 데이터 null")
                    Result.failure(Exception("주문 데이터가 없습니다"))
                }
            } else {
                Log.e("OrderRepository", "❌ 주문 조회 실패: ${response.code()}")
                Result.failure(Exception("주문 조회 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("OrderRepository", "❌ 주문 조회 에러: ${e.message}")
            Result.failure(e)
        }
    }
}