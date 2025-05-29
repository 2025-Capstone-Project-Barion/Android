// data/mapper/CategoryMapper.kt
package com.example.data.mapper

import com.example.data.dto.CategoryCreateRequest
import com.example.data.dto.CategoryDto
import com.example.domain.model.Category

/**
 * 카테고리 관련 데이터 변환을 담당하는 매퍼 함수들
 * DTO(Data Transfer Object)와 Domain Entity 간의 변환을 처리
 */

/**
 * 서버 응답 DTO를 Domain 모델로 변환
 *
 * @receiver CategoryDto 서버에서 받은 카테고리 데이터
 * @return Category Domain 계층에서 사용하는 카테고리 모델
 *
 * 변환 규칙:
 * - categoryId → id: 서버의 카테고리 ID를 Domain의 id로 매핑
 * - categoryName → name: 서버의 카테고리 이름을 Domain의 name으로 매핑
 * - order: API에 order 필드가 없으므로 categoryId를 Int로 변환하여 사용
 * - 나머지 필드들은 기본값 사용
 */
fun CategoryDto.toDomain(): Category {
    return Category(
        id = categoryId, // Long 그대로 사용
        name = categoryName,
        order = categoryId.toInt(), // Long을 Int로 변환 (order 필드용)
        // API에 없는 필드들은 기본값 설정
        isDefault = false,
        menuCount = 0,
        createdAt = System.currentTimeMillis()
    )
}

/**
 * Domain 모델을 서버 생성 요청 DTO로 변환
 *
 * @receiver Category Domain 계층의 카테고리 모델
 * @return CategoryCreateRequest 서버에 전송할 카테고리 생성 요청 데이터
 *
 * 변환 규칙:
 * - id → categoryId: Domain의 id를 서버의 categoryId로 매핑
 * - name → categoryName: Domain의 name을 서버의 categoryName으로 매핑
 */
fun Category.toCreateRequest(): CategoryCreateRequest {
    return CategoryCreateRequest(
        categoryId = id, // Long 그대로 사용
        categoryName = name
    )
}

