// :domain/src/main/java/com/example/domain/usecase/staff/UpdateStaffUseCase.kt
package com.example.domain.usecase.staff

import com.example.domain.model.Staff
import com.example.domain.repository.StaffRepository
import javax.inject.Inject

class UpdateStaffUseCase @Inject constructor(
    private val staffRepository: StaffRepository
) {
    suspend operator fun invoke(staff: Staff): Result<Staff> {
        // 동일한 검증 로직 적용
        if (staff.name.isBlank()) {
            return Result.failure(IllegalArgumentException("이름은 필수 입력입니다."))
        }

        if (staff.phoneNumber.isBlank() || !isValidPhoneNumber(staff.phoneNumber)) {
            return Result.failure(IllegalArgumentException("올바른 전화번호를 입력해주세요."))
        }

        if (staff.hourlyWage < 9620) {
            return Result.failure(IllegalArgumentException("시급은 최저임금 이상이어야 합니다."))
        }

        if (staff.accountNumber.isBlank()) {
            return Result.failure(IllegalArgumentException("계좌번호는 필수 입력입니다."))
        }

        return staffRepository.updateStaff(staff)
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val phonePattern = "^010\\d{8}$".toRegex()
        return phonePattern.matches(phoneNumber.replace("-", ""))
    }
}
