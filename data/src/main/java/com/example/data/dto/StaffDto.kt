// :data/src/main/java/com/example/data/dto/StaffDto.kt
package com.example.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 서버와 통신용 직원 DTO
 */
@Serializable
data class StaffDto(
    val id: Long,
    val name: String,
    @SerialName("phone_number")
    val phoneNumber: String,
    @SerialName("hourly_wage")
    val hourlyWage: Int,
    @SerialName("position_code")
    val positionCode: String,
    @SerialName("bank_code")
    val bankCode: String,
    @SerialName("account_number")
    val accountNumber: String,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)

/**
 * 직원 추가용 DTO
 */
@Serializable
data class CreateStaffDto(
    val name: String,
    @SerialName("phone_number")
    val phoneNumber: String,
    @SerialName("hourly_wage")
    val hourlyWage: Int,
    @SerialName("position_code")
    val positionCode: String,
    @SerialName("bank_code")
    val bankCode: String,
    @SerialName("account_number")
    val accountNumber: String
)

/**
 * 직원 수정용 DTO
 */
@Serializable
data class UpdateStaffDto(
    val name: String,
    @SerialName("phone_number")
    val phoneNumber: String,
    @SerialName("hourly_wage")
    val hourlyWage: Int,
    @SerialName("position_code")
    val positionCode: String,
    @SerialName("bank_code")
    val bankCode: String,
    @SerialName("account_number")
    val accountNumber: String
)

/**
 * 직원 목록 응답 DTO
 */
@Serializable
data class StaffListResponseDto(
    val data: List<StaffDto>,
    val total: Int,
    val page: Int? = null,
    val size: Int? = null
)