// data/dto/CategoryDto.kt
package com.example.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    @SerialName("categoryId")
    val categoryId: Long, // Int에서 Long으로 변경
    @SerialName("categoryName")
    val categoryName: String
)

@Serializable
data class CategoryCreateRequest(
    @SerialName("categoryId")
    val categoryId: Long, // Int에서 Long으로 변경
    @SerialName("categoryName")
    val categoryName: String
)

