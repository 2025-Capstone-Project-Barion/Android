// data/api/CategoryApi.kt - @Header 파라미터 방식
package com.example.data.api

import com.example.data.dto.CategoryCreateRequest
import com.example.data.dto.CategoryDto
import retrofit2.Response
import retrofit2.http.*

/**
 * 카테고리 관련 API 엔드포인트 정의
 */
interface CategoryApi {

    @GET("api/categories")
    suspend fun getCategories(): Response<List<CategoryDto>>

    /**
     * 새로운 카테고리 생성 - 헤더를 파라미터로 전달
     */
    @POST("api/categories")
    suspend fun createCategory(
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Accept") accept: String = "application/json",
        @Body request: CategoryCreateRequest
    ): Response<CategoryDto>

    @DELETE("api/categories/{id}")
    suspend fun deleteCategory(
        @Path("id") categoryId: Long
    ): Response<Unit>
}