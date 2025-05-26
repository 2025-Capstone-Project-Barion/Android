package com.example.domain.model

/**
 * 카테고리 도메인 엔티티
 * - 메뉴들을 그룹화하는 카테고리 정보
 * - 순서(order)를 통해 화면 표시 순서 관리
 */
data class Category(
    val id: Long = 0L,                    // 카테고리 고유 ID
    val name: String,                     // 카테고리 이름 (예: "추천", "커피")
    val order: Int,                       // 표시 순서 (1, 2, 3, 4...)
    val isDefault: Boolean = false,       // 기본 카테고리 여부 (삭제 불가)
    val menuCount: Int = 0,               // 해당 카테고리의 메뉴 개수
    val createdAt: Long = System.currentTimeMillis()
)