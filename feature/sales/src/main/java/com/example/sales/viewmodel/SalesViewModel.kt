package com.example.sales.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ChartData
import com.example.domain.model.SalesSummary
import com.example.domain.usecase.sale.GetMonthlySalesUseCase
import com.example.domain.usecase.sale.GetTotalSalesUseCase
import com.example.domain.usecase.sale.GetYearlySalesUseCase
import com.example.sales.type.SalesEffect
import com.example.sales.type.SalesIntent
import com.example.sales.type.SalesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


/**
 * 매출 관리 ViewModel
 * MVI 패턴으로 구현
 */
@HiltViewModel
class SalesViewModel @Inject constructor(
    private val getTotalSalesUseCase: GetTotalSalesUseCase,
    private val getYearlySalesUseCase: GetYearlySalesUseCase,
    private val getMonthlySalesUseCase: GetMonthlySalesUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "SalesViewModel"
    }

    // State
    private val _state = MutableStateFlow(SalesState())
    val state: StateFlow<SalesState> = _state.asStateFlow()

    // Effects
    private val _effect = Channel<SalesEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        Log.d(TAG, "🚀 SalesViewModel 초기화")
        handleIntent(SalesIntent.LoadSalesData)
    }

    /**
     * Intent 처리
     */
    fun handleIntent(intent: SalesIntent) {
        Log.d(TAG, "📩 Intent 수신: $intent")

        when (intent) {
            is SalesIntent.LoadSalesData -> loadSalesData()
            is SalesIntent.RefreshData -> refreshData()
            is SalesIntent.SelectMonth -> selectMonth(intent.month)
            is SalesIntent.SelectYear -> selectYear(intent.year)
        }
    }

    /**
     * 매출 데이터 로딩
     */
    private fun loadSalesData() {
        viewModelScope.launch {
            Log.d(TAG, "🔄 매출 데이터 로딩 시작")
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                // 병렬로 모든 데이터 조회
                val totalSalesResult = getTotalSalesUseCase()
                val yearlySalesResult = getYearlySalesUseCase()
                val monthlySalesResult = getMonthlySalesUseCase()

                // 결과 처리
                var hasError = false
                var errorMessage = ""

                if (totalSalesResult.isFailure) {
                    hasError = true
                    errorMessage = "총 매출 조회 실패"
                    Log.e(TAG, "❌ 총 매출 조회 실패", totalSalesResult.exceptionOrNull())
                }

                if (yearlySalesResult.isFailure) {
                    hasError = true
                    errorMessage = "연도별 매출 조회 실패"
                    Log.e(TAG, "❌ 연도별 매출 조회 실패", yearlySalesResult.exceptionOrNull())
                }

                if (monthlySalesResult.isFailure) {
                    hasError = true
                    errorMessage = "월별 매출 조회 실패"
                    Log.e(TAG, "❌ 월별 매출 조회 실패", monthlySalesResult.exceptionOrNull())
                }

                if (hasError) {
                    _state.value = _state.value.copy(isLoading = false, error = errorMessage)
                    _effect.send(SalesEffect.ShowError(errorMessage))
                    return@launch
                }

                // 성공 시 데이터 업데이트
                val totalSales = totalSalesResult.getOrNull()
                val yearlySales = yearlySalesResult.getOrNull() ?: emptyList()
                val monthlySales = monthlySalesResult.getOrNull() ?: emptyList()

                Log.d(TAG, "✅ 매출 데이터 로딩 성공")
                Log.d(TAG, "📊 총 매출: ${totalSales?.totalSales}")
                Log.d(TAG, "📊 연도별 매출: ${yearlySales.size}개")
                Log.d(TAG, "📊 월별 매출: ${monthlySales.size}개")

                // 요약 정보 계산
                val summary = SalesSummary.fromMonthlySales(monthlySales)
                Log.d(TAG, "📊 요약 정보 계산 완료: $summary")

                // 차트 데이터 생성
                val chartData = ChartData.fromMonthlySales(monthlySales)
                Log.d(TAG, "📊 차트 데이터 생성 완료: ${chartData.size}개")

                _state.value = _state.value.copy(
                    isLoading = false,
                    totalSales = totalSales,
                    yearlySales = yearlySales,
                    monthlySales = monthlySales,
                    salesSummary = summary,
                    chartData = chartData,
                    error = null
                )

            } catch (e: Exception) {
                Log.e(TAG, "❌ 매출 데이터 로딩 실패", e)
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "알 수 없는 오류"
                )
                _effect.send(SalesEffect.ShowError("매출 데이터를 불러오는데 실패했습니다."))
            }
        }
    }

    /**
     * 데이터 새로고침
     */
    private fun refreshData() {
        Log.d(TAG, "🔄 데이터 새로고침")
        loadSalesData()
    }

    /**
     * 월 선택
     */
    private fun selectMonth(month: Int) {
        Log.d(TAG, "📅 월 선택: ${month}월")

        // 차트 데이터 업데이트 (선택된 월 하이라이트)
        val updatedChartData = ChartData.fromMonthlySales(
            _state.value.monthlySales,
            highlightMonth = month
        )

        _state.value = _state.value.copy(
            selectedMonth = month,
            chartData = updatedChartData
        )

        Log.d(TAG, "✅ ${month}월 선택 완료")
    }

    /**
     * 연도 선택
     */
    private fun selectYear(year: Int) {
        Log.d(TAG, "📅 연도 선택: ${year}년")
        _state.value = _state.value.copy(selectedYear = year)

        // TODO: 연도별 데이터 필터링 또는 새로운 API 호출
        Log.d(TAG, "✅ ${year}년 선택 완료")
    }
}