package com.example.domain.usecase.menu

import com.example.domain.model.Category
import com.example.domain.repository.MenuRepository
import javax.inject.Inject

/**
 * 카테고리 추가 UseCase
 * - 카테고리 추가 시 필요한 비즈니스 로직 처리
 * - 입력 데이터 검증
 */
class AddCategoryUseCase @Inject constructor(
    private val menuRepository: MenuRepository
) {

    /**
     * 새 카테고리를 추가
     * @param name 카테고리 이름
     */
    suspend fun execute(name: String): Result<Category> {

        // 1. 입력 데이터 검증
        if (name.isBlank()) {
            return Result.failure(IllegalArgumentException("카테고리 이름은 필수입니다"))
        }

        if (name.length > 20) {
            return Result.failure(IllegalArgumentException("카테고리 이름은 20자 이하로 입력해주세요"))
        }

        // 2. 카테고리 추가
        return menuRepository.addCategory(name.trim())
    }
}