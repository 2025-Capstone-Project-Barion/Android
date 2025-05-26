package com.example.sales.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sales.type.SalesData
import com.example.sales.type.SalesEffect
import com.example.sales.type.SalesIntent
import com.example.sales.type.SalesPeriod
import com.example.sales.type.SalesState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class SalesViewModel : ViewModel() {

    private val _state = MutableStateFlow(SalesState())
    val state: StateFlow<SalesState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SalesEffect>()
    val effect: SharedFlow<SalesEffect> = _effect.asSharedFlow()

    init {
        handleIntent(SalesIntent.LoadSalesData)
    }

    fun handleIntent(intent: SalesIntent) {
        when (intent) {
            is SalesIntent.LoadSalesData -> loadSalesData()
            is SalesIntent.SelectPeriod -> selectPeriod(intent.period)
            is SalesIntent.RefreshData -> refreshData()
            is SalesIntent.ClearError -> clearError()
        }
    }

    private fun loadSalesData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                // 임시 데이터
                val salesData = getSampleSalesData()
                _state.value = _state.value.copy(
                    isLoading = false,
                    salesData = salesData
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "매출 데이터를 불러올 수 없습니다"
                )
            }
        }
    }

    private fun selectPeriod(period: SalesPeriod) {
        _state.value = _state.value.copy(selectedPeriod = period)
        loadSalesData()
    }

    private fun refreshData() = loadSalesData()

    private fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    private fun getSampleSalesData(): List<SalesData> {
        return listOf(
            SalesData(amount = 150000, orderCount = 25),
            SalesData(amount = 200000, orderCount = 30),
            SalesData(amount = 180000, orderCount = 28)
        )
    }
}

