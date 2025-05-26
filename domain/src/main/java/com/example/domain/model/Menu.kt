package com.example.domain.model

/**
 * 메뉴 도메인 엔티티
 * - 비즈니스 로직에서 사용하는 순수한 데이터 모델
 * - UI나 API에 의존하지 않음
 */
data class Menu(
    val id: Long = 0L,                    // 메뉴 고유 ID (서버에서 생성)
    val name: String,                     // 메뉴 이름 (예: "시그니처 커피")
    val price: Int,                       // 가격 (정수로 저장, 원 단위)
    val description: String = "",         // 메뉴 설명 (선택사항)
    val imageUrl: String = "",            // 이미지 URL (Base64 또는 서버 URL)
    val categoryId: Long,                 // 소속 카테고리 ID
    val isAvailable: Boolean = true,      // 판매 가능 여부
    val createdAt: Long = System.currentTimeMillis() // 생성 시간
)