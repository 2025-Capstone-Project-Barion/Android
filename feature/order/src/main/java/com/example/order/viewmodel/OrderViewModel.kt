package com.example.order.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import android.util.Log
import com.example.domain.usecase.order.DeleteOrderUseCase
import com.example.domain.usecase.order.GetAllOrdersUseCase
import com.example.domain.usecase.order.GetOrderUseCase
import com.example.order.type.OrderEffect
import com.example.order.type.OrderIntent
import com.example.order.type.OrderState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getAllOrdersUseCase: GetAllOrdersUseCase,
    private val deleteOrderUseCase: DeleteOrderUseCase,
    private val getOrderUseCase: GetOrderUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OrderState())
    val state: StateFlow<OrderState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<OrderEffect>()
    val effect: SharedFlow<OrderEffect> = _effect.asSharedFlow()

    // 현재 매장 ID (추후 설정 가능하도록)
    private val currentStoreId = 0 // 임시값

    init {
        Log.d("OrderViewModel", "🚀 ViewModel 초기화")
        // 실제 API 호출
        handleIntent(OrderIntent.LoadOrders)
    }

    fun handleIntent(intent: OrderIntent) {
        Log.d("OrderViewModel", "📩 Intent 수신: $intent")

        when (intent) {
            is OrderIntent.LoadOrders -> loadOrders()
            is OrderIntent.DeleteOrder -> deleteOrder(intent.orderId)
            is OrderIntent.RefreshData -> refreshData()
        }
    }

    private fun loadOrders() {
        viewModelScope.launch {
            Log.d("OrderViewModel", "📋 주문 목록 로딩 시작 (매장 ID: $currentStoreId)")
            _state.value = _state.value.copy(isLoading = true, error = null)

            getAllOrdersUseCase(currentStoreId)
                .onSuccess { orders ->
                    Log.d("OrderViewModel", "✅ 주문 목록 로딩 성공: ${orders.size}개")

                    val newState = _state.value.copy(
                        isLoading = false,
                        orders = orders,
                        error = null
                    )

                    // 요약 정보 자동 계산
                    val summary = newState.calculateSummary()

                    _state.value = newState.copy(summary = summary)

                    Log.d("OrderViewModel", "📊 요약 정보 계산 완료: $summary")
                }
                .onFailure { error ->
                    Log.e("OrderViewModel", "❌ 주문 목록 로딩 실패: ${error.message}")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                    _effect.emit(OrderEffect.ShowError("주문 목록을 불러올 수 없습니다"))
                }
        }
    }

    private fun deleteOrder(orderId: Int) {
        viewModelScope.launch {
            Log.d("OrderViewModel", "🗑️ 주문 삭제 시작: ID $orderId")

            deleteOrderUseCase(orderId)
                .onSuccess {
                    Log.d("OrderViewModel", "✅ 주문 삭제 성공: ID $orderId")
                    _effect.emit(OrderEffect.ShowDeleteSuccess("주문이 삭제되었습니다"))
                    // 삭제 후 데이터 새로고침
                    refreshData()
                }
                .onFailure { error ->
                    Log.e("OrderViewModel", "❌ 주문 삭제 실패: ${error.message}")
                    _effect.emit(OrderEffect.ShowError("주문 삭제에 실패했습니다"))
                }
        }
    }

    private fun refreshData() {
        Log.d("OrderViewModel", "🔄 데이터 새로고침")
        loadOrders()
    }

    // 매장 ID 설정 함수 (추후 사용)
    fun setStoreId(storeId: Int) {
        // TODO: storeId 설정 후 데이터 새로고침
    }
}