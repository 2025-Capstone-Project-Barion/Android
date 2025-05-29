// :data/src/main/java/com/example/data/dto/StaffDto.kt
package com.example.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 서버 응답용 직원 DTO - 실제 서버 스펙 반영
 */
@Serializable
data class StaffDto(
    @SerialName("employeeId")
    val employeeId: Long,

    @SerialName("storeId")
    val storeId: Long,

    @SerialName("employeeName")
    val employeeName: String,

    @SerialName("phoneNumber")
    val phoneNumber: String,

    @SerialName("salary")
    val salary: Int,  // 시급

    @SerialName("position")
    val position: String,

    @SerialName("bankAccount")
    val bankAccount: String
)

/**
 * 직원 생성용 DTO - POST 요청 시 사용
 */
@Serializable
data class CreateStaffDto(
    @SerialName("employeeId")
    val employeeId: Long = 0,  // 생성 시 0으로 전송

    @SerialName("storeId")
    val storeId: Long,

    @SerialName("employeeName")
    val employeeName: String,

    @SerialName("phoneNumber")
    val phoneNumber: String,

    @SerialName("salary")
    val salary: Int,

    @SerialName("position")
    val position: String,

    @SerialName("bankAccount")
    val bankAccount: String
)