// :data/src/main/java/com/example/data/api/StaffApi.kt
package com.example.data.api

import com.example.data.dto.CreateStaffDto
import com.example.data.dto.StaffDto
import com.example.data.dto.StaffListResponseDto
import com.example.data.dto.UpdateStaffDto
import retrofit2.Response
import retrofit2.http.*

/**
 * 직원 관리 API 인터페이스
 */
interface StaffApi {

    /**
     * 전체 직원 목록 조회
     */
    @GET("staff")
    suspend fun getStaffList(): Response<StaffListResponseDto>

    /**
     * 특정 직원 상세 정보 조회
     */
    @GET("staff/{id}")
    suspend fun getStaffById(@Path("id") id: Long): Response<StaffDto>

    /**
     * 새 직원 추가
     */
    @POST("staff")
    suspend fun createStaff(@Body createStaffDto: CreateStaffDto): Response<StaffDto>

    /**
     * 직원 정보 수정
     */
    @PUT("staff/{id}")
    suspend fun updateStaff(
        @Path("id") id: Long,
        @Body updateStaffDto: UpdateStaffDto
    ): Response<StaffDto>

    /**
     * 직원 삭제
     */
    @DELETE("staff/{id}")
    suspend fun deleteStaff(@Path("id") id: Long): Response<Unit>

    /**
     * 직원 검색 (이름 또는 전화번호)
     */
    @GET("staff/search")
    suspend fun searchStaff(@Query("q") query: String): Response<StaffListResponseDto>
}