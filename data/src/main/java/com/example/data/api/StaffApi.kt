// :data/src/main/java/com/example/data/api/StaffApi.kt
package com.example.data.api

import com.example.data.dto.CreateStaffDto
import com.example.data.dto.StaffDto
import retrofit2.Response
import retrofit2.http.*

/**
 * 직원 관리 API 인터페이스
 * 서버 스펙: http://13.209.99.95:8080/swagger-ui/index.html
 */
interface StaffApi {

    /**
     * 모든 직원 조회
     * GET /api/employees
     */
    @GET("api/employees")
    suspend fun getAllEmployees(): Response<List<StaffDto>>

    /**
     * 특정 직원 조회
     * GET /api/employees/{id}
     */
    @GET("api/employees/{id}")
    suspend fun getEmployeeById(@Path("id") id: Long): Response<StaffDto>

    /**
     * 직원 생성
     * POST /api/employees
     */
    @POST("api/employees")
    suspend fun createEmployee(@Body employee: CreateStaffDto): Response<StaffDto>

    /**
     * 직원 삭제
     * DELETE /api/employees/{id}
     */
    @DELETE("api/employees/{id}")
    suspend fun deleteEmployee(@Path("id") id: Long): Response<Unit>

    // 참고: 서버에 PUT 업데이트 API가 없어서 주석 처리
    // 필요하면 나중에 서버에 추가 요청
    /*
    @PUT("api/employees/{id}")
    suspend fun updateEmployee(
        @Path("id") id: Long,
        @Body employee: UpdateStaffDto
    ): Response<StaffDto>
    */
}