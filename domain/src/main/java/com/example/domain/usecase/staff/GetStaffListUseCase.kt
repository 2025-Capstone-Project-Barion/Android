// :domain/src/main/java/com/example/domain/usecase/staff/GetStaffListUseCase.kt
package com.example.domain.usecase.staff

import com.example.domain.model.Staff
import com.example.domain.repository.StaffRepository
import javax.inject.Inject

class GetStaffListUseCase @Inject constructor(
    private val staffRepository: StaffRepository
) {
    suspend operator fun invoke(): Result<List<Staff>> {
        return staffRepository.getStaffList()
    }
}