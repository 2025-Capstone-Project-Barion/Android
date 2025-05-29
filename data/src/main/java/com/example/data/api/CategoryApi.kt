// data/api/CategoryApi.kt
package com.example.data.api

import com.example.data.dto.CategoryCreateRequest
import com.example.data.dto.CategoryDto
import retrofit2.Response
import retrofit2.http.*

/**
 * 카테고리 관련 API 엔드포인트 정의
 * 카테고리 CRUD 작업을 처리하는 Retrofit 인터페이스
 */
interface CategoryApi {

    /**
     * 모든 카테고리 목록 조회
     *
     * @return 카테고리 목록을 담은 Response
     * API: GET /api/categories
     */
    @GET("api/categories")
    suspend fun getCategories(): Response<List<CategoryDto>>

    /**
     * 새로운 카테고리 생성
     *
     * @param request 생성할 카테고리 정보 (이름, ID)
     * @return 생성된 카테고리 정보를 담은 Response
     * API: POST /api/categories
     */
    @POST("api/categories")
    suspend fun createCategory(
        @Body request: CategoryCreateRequest
    ): Response<CategoryDto>

    /**
     * 특정 카테고리 삭제
     *
     * @param categoryId 삭제할 카테고리의 ID
     * @return 삭제 결과를 담은 Response (성공 시 빈 응답)
     * API: DELETE /api/categories/{id}
     */
    @DELETE("api/categories/{id}")
    suspend fun deleteCategory(
        @Path("id") categoryId: Long // Int에서 Long으로 변경
    ): Response<Unit>
}
