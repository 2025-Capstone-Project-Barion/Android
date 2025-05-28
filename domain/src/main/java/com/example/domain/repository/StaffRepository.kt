// :domain/src/main/java/com/example/domain/repository/StaffRepository.kt
package com.example.domain.repository

import com.example.domain.model.Staff
import kotlinx.coroutines.flow.Flow

/**
 * 직원 데이터 저장소 인터페이스
 */
interface StaffRepository {

    /**
     * 모든 직원 목록 조회
     */
    suspend fun getStaffList(): Result<List<Staff>>

    /**
     * 특정 직원 상세 정보 조회
     */
    suspend fun getStaffById(id: Long): Result<Staff>

    /**
     * 새 직원 추가
     */
    suspend fun addStaff(staff: Staff): Result<Staff>

    /**
     * 직원 정보 수정
     */
    suspend fun updateStaff(staff: Staff): Result<Staff>

    /**
     * 직원 삭제
     */
    suspend fun deleteStaff(id: Long): Result<Unit>

    /**
     * 이름 또는 전화번호로 직원 검색
     */
    suspend fun searchStaff(query: String): Result<List<Staff>>

    /**
     * 직원 목록 실시간 관찰 (선택사항)
     */
    fun observeStaffList(): Flow<List<Staff>>
}