// :data/src/main/java/com/example/data/mapper/StaffMapper.kt
package com.example.data.mapper

import com.example.data.dto.CreateStaffDto
import com.example.data.dto.StaffDto
import com.example.domain.model.Bank
import com.example.domain.model.Position
import com.example.domain.model.Staff

/**
 * StaffDto와 Staff Domain 모델 간 변환 - 실제 서버 스펙 반영
 */

// DTO -> Domain
fun StaffDto.toDomain(): Staff {
    return Staff(
        id = employeeId,
        name = employeeName,
        phoneNumber = phoneNumber,
        hourlyWage = salary,
        position = Position.fromDisplayName(position), // 서버의 "요리" -> Position.KITCHEN
        bank = Bank.fromAccountPattern(bankAccount),    // 계좌번호 패턴으로 은행 추정
        accountNumber = bankAccount,
        createdAt = null // 서버에서 제공하지 않음
    )
}

// Domain -> CreateStaffDto
fun Staff.toCreateDto(): CreateStaffDto {
    return CreateStaffDto(
        employeeId = 0, // 생성 시 0
        storeId = 1,    // 임시로 1 (나중에 실제 storeId로 변경)
        employeeName = name,
        phoneNumber = phoneNumber,
        salary = hourlyWage,
        position = position.displayName, // Position.KITCHEN -> "요리"
        bankAccount = accountNumber
    )
}

// DTO List -> Domain List
fun List<StaffDto>.toDomainList(): List<Staff> {
    return this.map { it.toDomain() }
}

// Position enum 확장 - 서버 응답 문자열과 매핑
fun Position.Companion.fromDisplayName(displayName: String): Position {
    return when (displayName) {
        "매니저" -> Position.MANAGER
        "바리스타" -> Position.BARISTA
        "캐셔" -> Position.CASHIER
        "요리" -> Position.KITCHEN
        "키오스크매니저" -> Position.KIOSK_MANAGER
        else -> Position.BARISTA // 기본값
    }
}

// Bank enum 확장 - 계좌번호 패턴으로 은행 추정 (임시)
fun Bank.Companion.fromAccountPattern(accountNumber: String): Bank {
    // 실제로는 더 정확한 로직 필요, 임시로 기본값 반환
    return Bank.KB // 기본값
}