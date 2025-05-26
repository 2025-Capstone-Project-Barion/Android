package com.example.order.viewmodel

// feature/order/src/main/java/com/barrion/feature/order/viewmodel/OrderViewModel.kt


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.order.type.Order
import com.example.order.type.OrderEffect
import com.example.order.type.OrderIntent
import com.example.order.type.OrderState
import com.example.order.type.OrderStatus
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {

    private val _state = MutableStateFlow(OrderState())
    val state: StateFlow<OrderState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<OrderEffect>()
    val effect: SharedFlow<OrderEffect> = _effect.asSharedFlow()

    init {
        handleIntent(OrderIntent.LoadOrders)
    }

    fun handleIntent(intent: OrderIntent) {
        when (intent) {
            is OrderIntent.LoadOrders -> loadOrders()
            is OrderIntent.RefreshOrders -> refreshOrders()
            is OrderIntent.SelectOrder -> selectOrder(intent.orderId)
            is OrderIntent.UpdateOrderStatus -> updateOrderStatus(intent.orderId, intent.status)
            is OrderIntent.ClearError -> clearError()
        }
    }

    private fun loadOrders() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                // 임시 데이터
                val orders = getSampleOrders()
                _state.value = _state.value.copy(
                    isLoading = false,
                    orders = orders
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "주문 데이터를 불러올 수 없습니다"
                )
            }
        }
    }

    private fun refreshOrders() = loadOrders()

    private fun selectOrder(orderId: String) {
        val order = _state.value.orders.find { it.id == orderId }
        _state.value = _state.value.copy(selectedOrder = order)

        viewModelScope.launch {
            _effect.emit(OrderEffect.NavigateToOrderDetail(orderId))
        }
    }

    private fun updateOrderStatus(orderId: String, status: OrderStatus) {
        _state.value = _state.value.copy(
            orders = _state.value.orders.map { order ->
                if (order.id == orderId) {
                    order.copy(status = status)
                } else {
                    order
                }
            }
        )

        viewModelScope.launch {
            _effect.emit(OrderEffect.ShowToast("주문 상태가 업데이트되었습니다"))
        }
    }

    private fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    private fun getSampleOrders(): List<Order> {
        return emptyList() // 일단 빈 리스트
    }
}