// data/dto/MenuDto.kt
package com.example.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuDto(
    @SerialName("menuId")
    val menuId: Long, // Int에서 Long으로 변경
    @SerialName("category")
    val category: Long, // Int에서 Long으로 변경
    @SerialName("menuName")
    val menuName: String,
    @SerialName("price")
    val price: Long, // Int에서 Long으로 변경
    @SerialName("cost")
    val cost: Long, // Int에서 Long으로 변경
    @SerialName("menuPresent")
    val menuPresent: String?, // String을 String?로 변경
    @SerialName("menuImage")
    val menuImage: String? = null,
    @SerialName("options")
    val options: List<String> = emptyList()
)

@Serializable
data class MenuCreateRequest(
    @SerialName("category")
    val category: Long,
    @SerialName("menuName")
    val menuName: String,
    @SerialName("price")
    val price: Long,
    @SerialName("cost")
    val cost: Long,
    @SerialName("menuPresent")
    val menuPresent: String, // String으로 되돌림 (서버가 String 기대)
    @SerialName("base64Image")
    val base64Image: String? = null
)

@Serializable
data class MenuUpdateRequest(
    @SerialName("category")
    val category: Long,
    @SerialName("menuName")
    val menuName: String,
    @SerialName("price")
    val price: Long,
    @SerialName("cost")
    val cost: Long,
    @SerialName("menuPresent")
    val menuPresent: String, // String으로 되돌림 (서버가 String 기대)
    @SerialName("base64Image")
    val base64Image: String? = null
)

@Serializable
data class MenuPageResponse(
    @SerialName("content")
    val content: List<MenuDto>,
    @SerialName("page")
    val page: Int? = null, // nullable로 변경
    @SerialName("size")
    val size: Int? = null, // nullable로 변경
    @SerialName("totalElements")
    val totalElements: Int? = null, // nullable로 변경
    @SerialName("totalPages")
    val totalPages: Int? = null // nullable로 변경
)