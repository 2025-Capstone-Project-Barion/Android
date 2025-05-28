// :domain/src/main/java/com/example/domain/usecase/staff/DeleteStaffUseCase.kt
package com.example.domain.usecase.staff

import com.example.domain.repository.StaffRepository
import javax.inject.Inject

class DeleteStaffUseCase @Inject constructor(
    private val staffRepository: StaffRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> {
        if (id <= 0) {
            return Result.failure(IllegalArgumentException("유효하지 않은 직원 ID입니다."))
        }

        return staffRepository.deleteStaff(id)
    }
}