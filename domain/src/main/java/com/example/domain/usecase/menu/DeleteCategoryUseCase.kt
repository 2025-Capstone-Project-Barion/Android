package com.example.domain.usecase.menu

import com.example.domain.repository.MenuRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val menuRepository: MenuRepository
) {
    suspend fun execute(categoryId: Long): Result<Unit> {
        // 기본 카테고리 삭제 방지
        if (categoryId <= 0) {
            return Result.failure(IllegalArgumentException("유효하지 않은 카테고리 ID입니다"))
        }

        return menuRepository.deleteCategory(categoryId)
    }
}