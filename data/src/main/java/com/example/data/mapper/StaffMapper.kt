// :data/src/main/java/com/example/data/mapper/StaffMapper.kt
package com.example.data.mapper

import com.example.data.dto.CreateStaffDto
import com.example.data.dto.StaffDto
import com.example.data.dto.UpdateStaffDto
import com.example.domain.model.Bank
import com.example.domain.model.Position
import com.example.domain.model.Staff

/**
 * StaffDto와 Staff Domain 모델 간 변환
 */

// DTO -> Domain
fun StaffDto.toDomain(): Staff {
    return Staff(
        id = id,
        name = name,
        phoneNumber = phoneNumber,
        hourlyWage = hourlyWage,
        position = Position.fromCode(positionCode),
        bank = Bank.fromCode(bankCode),
        accountNumber = accountNumber,
        createdAt = createdAt
    )
}

// Domain -> CreateStaffDto
fun Staff.toCreateDto(): CreateStaffDto {
    return CreateStaffDto(
        name = name,
        phoneNumber = phoneNumber,
        hourlyWage = hourlyWage,
        positionCode = position.code,
        bankCode = bank.code,
        accountNumber = accountNumber
    )
}

// Domain -> UpdateStaffDto
fun Staff.toUpdateDto(): UpdateStaffDto {
    return UpdateStaffDto(
        name = name,
        phoneNumber = phoneNumber,
        hourlyWage = hourlyWage,
        positionCode = position.code,
        bankCode = bank.code,
        accountNumber = accountNumber
    )
}

// DTO List -> Domain List
fun List<StaffDto>.toDomainList(): List<Staff> {
    return this.map { it.toDomain() }
}