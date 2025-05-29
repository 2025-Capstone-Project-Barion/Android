package com.example.domain.usecase.staff

import com.example.domain.model.Staff
import com.example.domain.repository.StaffRepository
import javax.inject.Inject

class AddStaffUseCase @Inject constructor(
    private val staffRepository: StaffRepository
) {
    suspend operator fun invoke(staff: Staff): Result<Staff> {
        // 비즈니스 로직 검증
        if (staff.name.isBlank()) {
            return Result.failure(IllegalArgumentException("이름은 필수 입력입니다."))
        }

        if (staff.phoneNumber.isBlank() || !isValidPhoneNumber(staff.phoneNumber)) {
            return Result.failure(IllegalArgumentException("올바른 전화번호를 입력해주세요."))
        }

        if (staff.hourlyWage < 9620) { // 2023년 최저임금 기준
            return Result.failure(IllegalArgumentException("시급은 최저임금 이상이어야 합니다."))
        }

        if (staff.accountNumber.isBlank()) {
            return Result.failure(IllegalArgumentException("계좌번호는 필수 입력입니다."))
        }

        return staffRepository.addStaff(staff)
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val phonePattern = "^010\\d{8}$".toRegex()
        return phonePattern.matches(phoneNumber.replace("-", ""))
    }
}