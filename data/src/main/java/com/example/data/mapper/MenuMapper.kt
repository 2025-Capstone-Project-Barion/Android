// data/mapper/MenuMapper.kt
package com.example.data.mapper

import com.example.data.dto.CategoryDto
import com.example.data.dto.MenuCreateRequest
import com.example.data.dto.MenuDto
import com.example.data.dto.MenuUpdateRequest
import com.example.domain.model.Category
import com.example.domain.model.Menu

/**
 * 메뉴 관련 데이터 변환을 담당하는 매퍼 함수들
 * DTO(Data Transfer Object)와 Domain Entity 간의 변환을 처리
 */

/**
 * 서버 응답 DTO를 Domain 모델로 변환
 *
 * @receiver MenuDto 서버에서 받은 메뉴 데이터
 * @return Menu Domain 계층에서 사용하는 메뉴 모델
 *
 * 변환 규칙:
 * - menuId → id: 서버의 메뉴 ID를 Domain의 id로 매핑
 * - menuName → name: 서버의 메뉴 이름을 Domain의 name으로 매핑
 * - price → price: 가격 정보 직접 매핑
 * - menuPresent → description: 서버의 메뉴 설명을 Domain의 description으로 매핑
 * - category → categoryId: 서버의 카테고리 ID를 Domain의 categoryId로 매핑
 * - menuImage → imageUrl: 서버의 이미지 URL을 Domain의 imageUrl로 매핑
 */
fun MenuDto.toDomain(): Menu {
    return Menu(
        id = menuId, // Long 그대로 사용
        name = menuName,
        price = price.toInt(), // Long을 Int로 변환
        description = menuPresent ?: "", // String?을 String으로 안전하게 변환 (null이면 빈 문자열)
        imageUrl = menuImage ?: "", // String?을 String으로 안전하게 변환 (null이면 빈 문자열)
        categoryId = category, // Long 그대로 사용
        // 나머지 필드들은 기본값 사용
        isAvailable = true,
        createdAt = System.currentTimeMillis()
    )
}

/**
 * Domain 모델을 서버 생성 요청 DTO로 변환
 *
 * @receiver Menu Domain 계층의 메뉴 모델
 * @param base64Image 업로드할 이미지의 Base64 인코딩 문자열 (선택사항)
 * @return MenuCreateRequest 서버에 전송할 메뉴 생성 요청 데이터
 *
 * 변환 규칙:
 * - categoryId → category: Domain의 categoryId를 서버의 category로 매핑
 * - name → menuName: Domain의 name을 서버의 menuName으로 매핑
 * - price → price: 가격 정보 직접 매핑
 * - description → menuPresent: Domain의 description을 서버의 menuPresent로 매핑
 * - cost: 원가 정보 (현재는 기본값 0 사용)
 * - base64Image: 새로 업로드하는 이미지 데이터
 */
fun Menu.toCreateRequest(base64Image: String?): MenuCreateRequest {
    return MenuCreateRequest(
        category = categoryId, // Long 그대로 사용
        menuName = name,
        price = price.toLong(), // Int를 Long으로 변환
        cost = 0L, // Long 타입으로 기본값 설정
        menuPresent = description, // String을 String으로 그대로 사용 (빈 문자열도 유효)
        base64Image = base64Image
    )
}

/**
 * Domain 모델을 서버 수정 요청 DTO로 변환
 *
 * @receiver Menu Domain 계층의 메뉴 모델
 * @param base64Image 새로 업로드할 이미지의 Base64 인코딩 문자열 (선택사항, null이면 기존 이미지 유지)
 * @return MenuUpdateRequest 서버에 전송할 메뉴 수정 요청 데이터
 *
 * 변환 규칙:
 * - toCreateRequest와 동일한 매핑 규칙 적용
 * - base64Image가 null이면 서버에서 기존 이미지를 유지함
 */
fun Menu.toUpdateRequest(base64Image: String?): MenuUpdateRequest {
    return MenuUpdateRequest(
        category = categoryId, // Long 그대로 사용
        menuName = name,
        price = price.toLong(), // Int를 Long으로 변환
        cost = 0L, // Long 타입으로 기본값 설정
        menuPresent = description, // String을 String으로 그대로 사용
        base64Image = base64Image // null이면 기존 이미지 유지
    )
}

/**
 * 메뉴 리스트를 Domain 모델 리스트로 변환하는 확장 함수
 *
 * @receiver List<MenuDto> 서버에서 받은 메뉴 DTO 리스트
 * @return List<Menu> Domain 계층에서 사용하는 메뉴 모델 리스트
 *
 * 사용 예: menuDtoList.toMenuDomainList()
 */
fun List<MenuDto>.toMenuDomainList(): List<Menu> {
    return this.map { it.toDomain() }
}

/**
 * 카테고리 리스트를 Domain 모델 리스트로 변환하는 확장 함수
 *
 * @receiver List<CategoryDto> 서버에서 받은 카테고리 DTO 리스트
 * @return List<Category> Domain 계층에서 사용하는 카테고리 모델 리스트
 *
 * 사용 예: categoryDtoList.toCategoryDomainList()
 */
fun List<CategoryDto>.toCategoryDomainList(): List<Category> {
    return this.map { it.toDomain() }
}