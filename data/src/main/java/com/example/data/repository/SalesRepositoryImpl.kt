package com.example.data.repository

// data/repository/SalesRepositoryImpl.kt


import android.util.Log
import com.example.data.api.SalesApi
import com.example.data.mapper.toDomain
import com.example.domain.model.SalesData
import com.example.domain.model.TotalSales
import com.example.domain.repository.SalesRepository
import javax.inject.Inject

/**
 * 매출 데이터 리포지토리 구현체
 */
class SalesRepositoryImpl @Inject constructor(
    private val salesApi: SalesApi
) : SalesRepository {

    companion object {
        private const val TAG = "SalesRepository"
    }

    override suspend fun getTotalSales(): Result<TotalSales> {
        return try {
            Log.d(TAG, "🔍 총 매출 조회 시작")

            val response = salesApi.getTotalSales()
            Log.d(TAG, "📡 총 매출 응답: $response")

            val totalSales = response.toDomain()
            Log.d(TAG, "✅ 총 매출 변환 성공: $totalSales")

            Result.success(totalSales)
        } catch (e: Exception) {
            Log.e(TAG, "❌ 총 매출 조회 실패", e)
            Result.failure(e)
        }
    }

    override suspend fun getYearlySales(): Result<List<SalesData>> {
        return try {
            Log.d(TAG, "🔍 연도별 매출 조회 시작")

            val response = salesApi.getYearlySales()
            Log.d(TAG, "📡 연도별 매출 응답: ${response.size}개 데이터")

            val yearlySales = response.toDomain()
            Log.d(TAG, "✅ 연도별 매출 변환 성공: ${yearlySales.size}개")

            Result.success(yearlySales)
        } catch (e: Exception) {
            Log.e(TAG, "❌ 연도별 매출 조회 실패", e)
            Result.failure(e)
        }
    }

    override suspend fun getMonthlySales(): Result<List<SalesData>> {
        return try {
            Log.d(TAG, "🔍 월별 매출 조회 시작")

            val response = salesApi.getMonthlySales()
            Log.d(TAG, "📡 월별 매출 응답: ${response.size}개 데이터")
            response.forEach { data ->
                Log.d(TAG, "📊 월별 데이터: ${data.salesDate} - ${data.totalSales}원")
            }

            val monthlySales = response.toDomain()
            Log.d(TAG, "✅ 월별 매출 변환 성공: ${monthlySales.size}개")

            Result.success(monthlySales)
        } catch (e: Exception) {
            Log.e(TAG, "❌ 월별 매출 조회 실패", e)
            Result.failure(e)
        }
    }
}