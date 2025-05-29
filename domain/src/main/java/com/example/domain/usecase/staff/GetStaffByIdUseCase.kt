// :domain/src/main/java/com/example/domain/usecase/staff/GetStaffByIdUseCase.kt
package com.example.domain.usecase.staff

import com.example.domain.model.Staff
import com.example.domain.repository.StaffRepository
import javax.inject.Inject

class GetStaffByIdUseCase @Inject constructor(
    private val staffRepository: StaffRepository
) {
    suspend operator fun invoke(id: Long): Result<Staff> {
        return staffRepository.getStaffById(id)
    }
}