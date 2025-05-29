// :domain/src/main/java/com/example/domain/usecase/staff/SearchStaffUseCase.kt
package com.example.domain.usecase.staff

import com.example.domain.model.Staff
import com.example.domain.repository.StaffRepository
import javax.inject.Inject

class SearchStaffUseCase @Inject constructor(
    private val staffRepository: StaffRepository
) {
    suspend operator fun invoke(query: String): Result<List<Staff>> {
        val trimmedQuery = query.trim()

        if (trimmedQuery.isBlank()) {
            // 빈 검색어면 전체 목록 반환
            return staffRepository.getStaffList()
        }

        if (trimmedQuery.length < 2) {
            return Result.failure(IllegalArgumentException("검색어는 2글자 이상 입력해주세요."))
        }

        return staffRepository.searchStaff(trimmedQuery)
    }
}