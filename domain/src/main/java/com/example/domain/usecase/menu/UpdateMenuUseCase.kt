package com.example.domain.usecase.menu

import com.example.domain.model.Menu
import com.example.domain.repository.MenuRepository
import javax.inject.Inject

/**
 * 메뉴 수정 UseCase
 * - 메뉴 수정 시 필요한 비즈니스 로직 처리
 * - 입력 데이터 검증
 * - 이미지 포함/미포함 수정 지원
 */
class UpdateMenuUseCase @Inject constructor(
    private val menuRepository: MenuRepository
) {

    /**
     * 메뉴를 수정 (이미지 없음)
     * @param menu 수정할 메뉴 객체
     */
    suspend fun execute(menu: Menu): Result<Menu> {

        // 1. 입력 데이터 검증
        val validationResult = validateMenu(menu)
        if (validationResult.isFailure) {
            return validationResult
        }

        // 2. 메뉴 수정 (이미지 없음)
        return menuRepository.updateMenu(menu)
    }

    /**
     * 메뉴를 수정 (이미지 포함)
     * @param menu 수정할 메뉴 객체
     * @param base64Image Base64로 인코딩된 이미지 데이터
     */
    suspend fun executeWithImage(menu: Menu, base64Image: String): Result<Menu> {

        // 1. 입력 데이터 검증
        val validationResult = validateMenu(menu)
        if (validationResult.isFailure) {
            return validationResult
        }

        // 2. Base64 이미지 검증
        if (base64Image.isBlank()) {
            return Result.failure(IllegalArgumentException("이미지 데이터가 비어있습니다"))
        }

        if (!base64Image.startsWith("data:image")) {
            return Result.failure(IllegalArgumentException("올바른 이미지 형식이 아닙니다"))
        }

        // 3. 메뉴 수정 (이미지 포함)
        return menuRepository.updateMenuWithImage(menu, base64Image)
    }

    /**
     * 메뉴 데이터 검증
     * @param menu 검증할 메뉴 객체
     * @return 검증 결과 - 성공 시 원본 메뉴, 실패 시 에러
     */
    private fun validateMenu(menu: Menu): Result<Menu> {
        if (menu.name.isBlank()) {
            return Result.failure(IllegalArgumentException("메뉴 이름은 필수입니다"))
        }

        if (menu.price < 0) {
            return Result.failure(IllegalArgumentException("가격은 0 이상이어야 합니다"))
        }

        if (menu.categoryId <= 0) {
            return Result.failure(IllegalArgumentException("올바른 카테고리를 선택해주세요"))
        }

        if (menu.id <= 0) {
            return Result.failure(IllegalArgumentException("올바른 메뉴 ID가 아닙니다"))
        }

        return Result.success(menu)
    }
}