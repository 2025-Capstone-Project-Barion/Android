package com.example.domain.usecase.menu

import com.example.domain.model.Menu
import com.example.domain.repository.MenuRepository
import javax.inject.Inject

/**
 * 메뉴 추가 UseCase
 * - 메뉴 추가 시 필요한 비즈니스 로직 처리
 * - 입력 데이터 검증 및 메뉴 생성
 */
class AddMenuUseCase @Inject constructor(
    private val menuRepository: MenuRepository
) {

    /**
     * 새 메뉴를 추가
     * @param name 메뉴 이름
     * @param price 가격
     * @param categoryId 카테고리 ID
     * @param description 설명 (선택사항)
     * @param imageUrl 이미지 URL (선택사항)
     * @param base64Image Base64 인코딩된 이미지 데이터 (선택사항)
     */
    suspend fun execute(
        name: String,
        price: Int,
        categoryId: Long,
        description: String = "",
        imageUrl: String = "",
        base64Image: String? = null  // 추가
    ): Result<Menu> {

        // 1. 입력 데이터 검증
        if (name.isBlank()) {
            return Result.failure(IllegalArgumentException("메뉴 이름은 필수입니다"))
        }

        if (price < 0) {
            return Result.failure(IllegalArgumentException("가격은 0 이상이어야 합니다"))
        }

        if (categoryId <= 0) {
            return Result.failure(IllegalArgumentException("올바른 카테고리를 선택해주세요"))
        }

        // 2. 메뉴 객체 생성
        val newMenu: Menu = Menu(
            name = name.trim(),
            price = price,
            categoryId = categoryId,
            description = description.trim(),
            imageUrl = imageUrl
        )

        // 3. 저장 (base64Image도 함께 전달)
        // 참고: Repository의 addMenu 메서드도 base64Image 파라미터가 필요함
        return menuRepository.addMenu(newMenu, base64Image)
    }
}