package com.example.domain.usecase.menu

import com.example.domain.model.Menu
import com.example.domain.repository.MenuRepository
import javax.inject.Inject

/**
 * 메뉴 수정 UseCase
 * - 메뉴 수정 시 필요한 비즈니스 로직 처리
 * - 입력 데이터 검증
 */
class UpdateMenuUseCase @Inject constructor(
    private val menuRepository: MenuRepository
) {

    /**
     * 메뉴를 수정
     * @param menu 수정할 메뉴 객체
     */
    suspend fun execute(menu: Menu): Result<Menu> {

        // 1. 입력 데이터 검증
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

        // 2. 메뉴 수정
        return menuRepository.updateMenu(menu)
    }
}